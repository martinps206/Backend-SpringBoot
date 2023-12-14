package com.proyectoDH.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.proyectoDH.config.UserAuthenticationProvider;
import com.proyectoDH.dto.*;
import com.proyectoDH.entities.*;
import com.proyectoDH.repository.*;
import com.proyectoDH.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE,RequestMethod.PUT})
public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);


    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final ICategoriaService iCategoriaService;
    private final IProductoService iProductoService;
    private final ITalleService iTalleService;
    private final IColorService iColorService;
    private final ICaracteristicaService iCaracteristicaService;
    private final EmailService emailService;
    private final ProductoReservaService productoReservaService;
    private final ProductoService productoService;

    private final ProductoRepository productoRepository;
    private final TalleRepository talleRepository;
    private final ColorRepository colorRepository;
    private final CategoriaRepository categoriaRepository;
    private final CaracteristicaRepository caracteristicaRepository;
    private final ImagenRepository imagenRepository;
    private final ProductoReservaRepository productoReservaRepository;

    private final String imageFolderPath = "src/main/resources/static/images/";

    /************ REGISTRO-LOGIN ***********/

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid CredentialsDto credentialsDto) {
        UserDto userDto = userService.login(credentialsDto);
        userDto.setToken(userAuthenticationProvider.createToken(userDto));
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid SignUpDto user) {

        UserDto createdUser = userService.register(user);
        createdUser.setToken(userAuthenticationProvider.createToken(createdUser));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }

    @PostMapping("/{id}/login")
    public ResponseEntity<String> getLoginById(@PathVariable Long id) {
        String login = userService.findLoginById(id);
        return ResponseEntity.ok(login);
    }

    @PutMapping("/{userId}/admin")
    public ResponseEntity<User> updateAdminStatus(
            @PathVariable Long userId,
            @RequestBody Map<String, Boolean> adminStatus) throws ChangeSetPersister.NotFoundException {
        Boolean newAdminStatus = adminStatus.get("admin");
        User updatedUser = userService.updateAdminStatus(userId, newAdminStatus);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<List<UsersDto>> getAllUsers() {
        List<UsersDto> usersDtos = userService.getAllUsers();

        if (!usersDtos.isEmpty()) {
            return new ResponseEntity<>(usersDtos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /********EMAIL*********/

    public String generateConfirmationToken() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }

    @PostMapping("/register-user")
    public ResponseEntity<UserDto> registerUser(@RequestBody @Valid SignUpDto user) {
        UserDto createdUser = userService.register(user);
        String confirmationToken = generateConfirmationToken();
        userService.saveConfirmationToken(createdUser.getId(), confirmationToken);
        emailService.sendConfirmationEmail(createdUser.getLogin(), confirmationToken);
        createdUser.setToken(userAuthenticationProvider.createToken(createdUser));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }

    /* CATEGORIAS */

    @GetMapping("/categorias")
    public List<Categoria> getAllCategorias() {
        return iCategoriaService.getAllCategorias();
    }

    @PostMapping("/categorias")
    public Categoria addCategoria(@RequestBody Categoria nuevaCategoria) {
        return iCategoriaService.agregarCategoria(nuevaCategoria);
    }

    @PutMapping("/categorias/{codigo}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable String codigo, @RequestBody Categoria categoriaActualizada){
        Categoria categoria = iCategoriaService.actualizarCategoria(codigo, categoriaActualizada);
        return ResponseEntity.ok(categoria);
    }

    @DeleteMapping("/categoria/{codigo}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable String codigo) {
        iCategoriaService.eliminarCategoria(codigo);
        return ResponseEntity.noContent().build();
    }

    /* PRODUCTOS */

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable String id) {
        iProductoService.eliminarProducto(id);
        return ResponseEntity.ok("Producto eliminado correctamente.");
    }

    @PostMapping("/productos")
    public ResponseEntity<List<ProductoConImagenDTO>> obtenerProductos() {
        List<Producto> productos = productoRepository.findAll();

        // Mapear cada Producto a ProductoConImagenDTO
        List<ProductoConImagenDTO> productosDTO = productos.stream()
                .map(this::mapearProductoAProductoConImagenDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(productosDTO, HttpStatus.OK);
    }

    private ProductoConImagenDTO mapearProductoAProductoConImagenDTO(Producto producto) {
        ProductoConImagenDTO productoDTO = new ProductoConImagenDTO();
        productoDTO.setCodigo(producto.getCodigo());
        productoDTO.setNombre(producto.getNombre());
        productoDTO.setDescripcion(producto.getDescripcion());

        productoDTO.setTalles(mapearTalles(producto.getTalles()));
        productoDTO.setColores(mapearColores(producto.getColores()));
        productoDTO.setCategorias(mapearCategorias(producto.getCategorias()));
        productoDTO.setImagenes(mapearImagenes(producto.getImagenes()));
        productoDTO.setCaracteristicas(mapearCaracteristicas(producto.getCaracteristicas()));

        return productoDTO;
    }

    @GetMapping("/producto/{codigo}")
    public ResponseEntity<ProductoConImagenDTO> obtenerProductoPorCodigo(@PathVariable String codigo) {
        Optional<Producto> productoOptional = productoRepository.findById(codigo);

        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();

            ProductoConImagenDTO productoDTO = new ProductoConImagenDTO();
            productoDTO.setCodigo(producto.getCodigo());
            productoDTO.setNombre(producto.getNombre());
            productoDTO.setDescripcion(producto.getDescripcion());

            productoDTO.setTalles(mapearTalles(producto.getTalles()));
            productoDTO.setColores(mapearColores(producto.getColores()));
            productoDTO.setCategorias(mapearCategorias(producto.getCategorias()));
            productoDTO.setImagenes(mapearImagenes(producto.getImagenes()));
            productoDTO.setCaracteristicas(mapearCaracteristicas(producto.getCaracteristicas()));


            return new ResponseEntity<>(productoDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    private Set<TalleDTO> mapearTalles(Set<Talle> talles) {
        return talles.stream()
                .map(talle -> new TalleDTO(talle.getCodigo(), talle.getDescripcion()))
                .collect(Collectors.toSet());
    }

    private Set<ColorDTO> mapearColores(Set<Color> colores) {
        return colores.stream()
                .map(color -> new ColorDTO(color.getCodigo(), color.getDescripcion()))
                .collect(Collectors.toSet());
    }

    private Set<CategoriaDTO> mapearCategorias(Set<Categoria> categorias) {
        return categorias.stream()
                .map(categoria -> new CategoriaDTO(categoria.getCodigo(), categoria.getNombre(), categoria.getDescripcion(), categoria.getFoto()))
                .collect(Collectors.toSet());
    }

    private Set<ImagenDTO> mapearImagenes(Set<Imagen> imagenes) {
        return imagenes.stream()
                .map(imagen -> new ImagenDTO(imagen.getCodigo(), imagen.getDescripcion(), imagen.getBase64Imagen()))
                .collect(Collectors.toSet());
    }

    private Set<CaracteristicaDTO> mapearCaracteristicas(Set<Caracteristica> caracteristicas) {
        return caracteristicas.stream()
                .map(caracteristica -> new CaracteristicaDTO(caracteristica.getCodigo(), caracteristica.getNombre(), caracteristica.getIcono()))
                .collect(Collectors.toSet());
    }

    @PostMapping("/agregar")
    public ResponseEntity<?> agregarProducto(@RequestBody ProductoDTO productoDTO) {
        try {
            Producto producto = iProductoService.agregarProducto(productoDTO);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/agregardata")
    public ResponseEntity<?> agregarProductoData(
            @RequestPart("productoDTO") ProductoDTO productoDTO,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        try {
            // Tu lógica para manejar el ProductoDTO y la imagen
            Producto producto = iProductoService.agregarProductoData(productoDTO, imagen);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/modificarproducto/{codigo}")
    public ResponseEntity<Producto> modificarProducto(
            @PathVariable String codigo,
            @RequestBody ProductoDTO productoDTO) {

        Optional<Producto> productoOptional = productoRepository.findById(codigo);

        return productoOptional.map(productoExistente -> {
            productoExistente.setNombre(productoDTO.getNombre());
            productoExistente.setDescripcion(productoDTO.getDescripcion());

            // Actualiza las entidades Talle correspondientes a los códigos proporcionados.
            List<Talle> talles = talleRepository.findAllByCodigoIn(productoDTO.getTalles());
            productoExistente.setTalles(new HashSet<>(talles));

            // Actualiza las entidades Color correspondientes a los códigos proporcionados.
            List<Color> colores = colorRepository.findAllByCodigoIn(productoDTO.getColores());
            productoExistente.setColores(new HashSet<>(colores));

            // Actualiza las entidades Categoría correspondientes a los códigos proporcionados.
            List<Categoria> categorias = categoriaRepository.findAllByCodigoIn(productoDTO.getCategorias());
            productoExistente.setCategorias(new HashSet<>(categorias));

            List<Imagen> imagenes = imagenRepository.findAllByCodigoIn(productoDTO.getImagenes());
            productoExistente.setImagenes(new HashSet<>(imagenes));

            List<Caracteristica> caracteristicas = caracteristicaRepository.findAllByCodigoIn(productoDTO.getCaracteristicas());
            productoExistente.setCaracteristicas(new HashSet<>(caracteristicas));

            // Guarda el producto actualizado en la base de datos.
            Producto productoActualizado = productoRepository.save(productoExistente);

            return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /* TALLE */

    @PostMapping("/agregarTalle")
    public ResponseEntity<Talle> agregarTalle(@RequestBody Talle talle) {
        Talle createdTalle = iTalleService.createTalle(talle);
        return new ResponseEntity<>(createdTalle, HttpStatus.CREATED);
    }

    @PostMapping("/talles")
    public List<TalleDTO> getAllTalles() {
        return iTalleService.getAllTalles();
    }


    /* COLORES */

    @PostMapping("/colores")
    public List<ColorDTO> getAllColores() {
        return iColorService.getAllColores();
    }


    /* IMAGENES */

    @GetMapping("/images/{imageName}")
    public ResponseEntity<Resource> serveFile(@PathVariable String imageName) {
        Path imagePath = Paths.get(imageFolderPath).resolve(imageName);
        Resource resource;
        try {
            resource = new UrlResource(imagePath.toUri());
        } catch (IOException e) {
            throw new RuntimeException("Could not read the image!");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + imageName)
                .body(resource);
    }

    private final ImagenService imagenService;

    @PostMapping("/agregarimagen")
    public ResponseEntity<Void> agregarListaImagenes(@RequestBody List<ImagenDTO> imagenesDTO) {
        imagenService.guardarListaImagenes(imagenesDTO);
        return ResponseEntity.noContent().build();
    }

    /* CARACTERISTICAS */

    @PostMapping("/caracteristicas")
    public List<Caracteristica> getAllCaracteristicas() {
        return iCaracteristicaService.getAllCaracteristicas();
    }

    @PutMapping("/caracteristicas/{codigo}")
    public ResponseEntity<Caracteristica> actualizarCaracteristica(@PathVariable String codigo, @RequestBody Caracteristica caracteristicaActualizada) {
        Caracteristica caracteristica = iCaracteristicaService.actualizarCaracteristica(codigo, caracteristicaActualizada);
        return ResponseEntity.ok(caracteristica);
    }

    @DeleteMapping("/caracteristicas/{codigo}")
    public ResponseEntity<Void> eliminarCaracteristica(@PathVariable String codigo) {
        iCaracteristicaService.eliminarCaracteristica(codigo);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/agregarcaracteristica")
    public ResponseEntity<Caracteristica> agregarCaracteristica(@RequestBody CaracteristicaDTO caracteristicaDTO) {
        Caracteristica nuevaCaracteristica = iCaracteristicaService.agregarCaracteristica(caracteristicaDTO);
        return ResponseEntity.ok(nuevaCaracteristica);
    }

    /***** PRODUCTO RESERVA ******/

    @GetMapping("/productosReservados/{idUsuario}")
    public ResponseEntity<List<ProductoReserva>> obtenerProductosReservadosPorUsuario(@PathVariable Long idUsuario) {
        List<ProductoReserva> productosReservados = productoReservaRepository.findByIdUsuario(idUsuario);
        return new ResponseEntity<>(productosReservados, HttpStatus.OK);
    }

    @GetMapping("/productosReservados")
    public ResponseEntity<List<ProductoReserva>> obtenerProductosReservados() {
        List<ProductoReserva> productosReservados = productoReservaRepository.findAll();
        return new ResponseEntity<>(productosReservados, HttpStatus.OK);
    }


    @GetMapping("/productosNoReservados/fechas")
    public ResponseEntity<List<Producto>> obtenerProductosNoReservados(
            @RequestParam("fecha1") String fecha1,
            @RequestParam("fecha2") String fecha2) {

        List<Producto> productosNoReservados = iProductoService.getProductosNoReservados(fecha1, fecha2);

        return new ResponseEntity<>(productosNoReservados, HttpStatus.OK);
    }


    @GetMapping("/productosNoReservados")
    public ResponseEntity<List<Producto>> obtenerProductosNoReservados(
            @RequestParam("producto") String producto,
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd,
            @RequestParam("color") String color,
            @RequestParam("talle") String talle) {


        List<Producto> productosNoReservados = iProductoService.getProductosNoReservadosTotal(producto, color, talle, dateStart, dateEnd);

        return new ResponseEntity<>(productosNoReservados, HttpStatus.OK);
    }

    @GetMapping("/productosReservadosUsiario/{codigoProducto}")
    public ResponseEntity<?> obtenerPorIdProducto(@PathVariable String codigoProducto) {
        List<ProductoReserva> productoReservas = productoReservaService.obtenerPorIdProducto(codigoProducto);

        if (productoReservas.isEmpty()) {
            return new ResponseEntity<>("No se encontraron reservas para el producto con código " + codigoProducto, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(productoReservas, HttpStatus.OK);
        }
    }

    @PostMapping("/buscarpor")
    public ResponseEntity<List<Producto>> buscarProductos(@RequestBody FiltroBusqueda filtro) {
        log.info("Recibiendo solicitud de búsqueda con filtro: {}", filtro);

        List<Producto> productos = productoService.buscarProductosDisponibles(filtro);

        log.info("Número de productos encontrados: {}", productos.size());
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @PostMapping("/productosNoReservados-alta")
    public ResponseEntity<String> altaProductoReserva(@RequestBody ReservaDTO reservaDTO) {
        try {
            productoReservaService.altaProductoReserva(
                    reservaDTO.getCodiProducto(),
                    reservaDTO.getCodiUsuario(),
                    reservaDTO.getFechaDesdeBus(),
                    reservaDTO.getFechaHastaBus(),
                    reservaDTO.getCodiTalle(),
                    reservaDTO.getCodiColor()
            );
            return ResponseEntity.ok("Procedimiento almacenado ejecutado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al ejecutar el procedimiento almacenado: " + e.getMessage());
        }
    }

    @DeleteMapping("/productosNoReservados-baja/{idReserva}")
    public ResponseEntity<String> bajaProductoReserva(@PathVariable Long idReserva) {
        try {
            productoReservaService.bajaProductoReserva(idReserva);
            return ResponseEntity.ok("Procedimiento almacenado ejecutado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al ejecutar el procedimiento almacenado: " + e.getMessage());
        }
    }

    @GetMapping("/consultaproductosNoReservados/{codiUsuario}")
    public ResponseEntity<List<ProductoReserva>> consultarReservasPorUsuario(@PathVariable Long codiUsuario) {
        List<ProductoReserva> reservas = productoReservaService.consultarReservasPorUsuario(codiUsuario);
        return ResponseEntity.ok(reservas);
    }


    @Autowired
    private GeneroService generoService;

    @GetMapping("/generos")
    public ResponseEntity<List<GeneroDTO>> obtenerTodosLosGeneros() {
        List<GeneroDTO> generos = generoService.obtenerTodosLosGeneros();
        return ResponseEntity.ok(generos);
    }

    @GetMapping("/generos/{codigo}")
    public ResponseEntity<Genero> obtenerGeneroPorCodigo(@PathVariable String codigo) {
        Genero genero = generoService.obtenerGeneroPorCodigo(codigo);
        return ResponseEntity.ok(genero);
    }


    @PostMapping("/agregar-con-imagen")
    public ResponseEntity<?> agregarProductoConImagen(@RequestBody ProductoDTO requestDTO) {
        try {
            Producto producto = iProductoService.agregarProducto(requestDTO);
            //imagenService.guardarImagen(requestDTO.getDescripcion(), requestDTO.getBase64Imagen(), requestDTO.getSecuencia(), producto);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @Autowired
    private FavoritosService favoritosService;

    @PostMapping("/agregarfavoritos")
    public ResponseEntity<String> agregarProductoAFavoritos(
            @RequestParam Long userId,
            @RequestParam List<String> productoCodigos) {

        try {
            System.out.println("martin user id 0 " + userId);
            favoritosService.agregarProductoAFavoritos(userId, productoCodigos);
            return ResponseEntity.ok("Productos agregados a favoritos exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al agregar productos a favoritos: " + e.getMessage());
        }
    }


    @GetMapping("/favoritos/{userId}")
    public ResponseEntity<List<FavoritoDTO>> obtenerFavoritosDTOPorUsuario(@PathVariable Long userId) {
        List<FavoritoDTO> favoritosDTO = favoritosService.obtenerFavoritosDTOPorUsuario(userId);
        return ResponseEntity.ok(favoritosDTO);
    }

    @DeleteMapping("/eliminarfavoritos")
    public ResponseEntity<String> eliminarFavoritosPorUsuario(@RequestParam Long userId) {
        try {
            favoritosService.eliminarFavoritosPorUsuario(userId);
            return ResponseEntity.ok("Favoritos eliminados exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar favoritos: " + e.getMessage());
        }
    }


}
