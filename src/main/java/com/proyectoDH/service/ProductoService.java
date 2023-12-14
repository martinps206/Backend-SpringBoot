package com.proyectoDH.service;

import org.apache.commons.io.IOUtils;
import com.proyectoDH.dto.ImagenDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.proyectoDH.dto.FiltroBusqueda;
import com.proyectoDH.dto.ProductoDTO;
import com.proyectoDH.entities.*;
import com.proyectoDH.repository.*;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import java.util.stream.Collectors;


@Service
public class ProductoService implements IProductoService{

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    public TalleRepository talleRepository;

    @Autowired
    public ColorRepository colorRepository;

    @Autowired
    public CategoriaRepository categoriaRepository;

    @Autowired
    public ImagenRepository imagenRepository;

    @Autowired
    public CaracteristicaRepository caracteristicaRepository;

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto save(Producto producto) {
        return null;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void eliminarProducto(String id) {
        // Elimina el producto de la tabla 'producto'
        productoRepository.deleteById(id);
    }

    @Transactional
    private Imagen convertirDTOaEntidad(ImagenDTO imagenDTO) {
        Imagen imagen = new Imagen();
        imagen.setCodigo(imagenDTO.getCodigo());
        imagen.setDescripcion(imagenDTO.getDescripcion());
        imagen.setBase64Imagen(imagenDTO.getBase64Imagen());
        // Puedes configurar otras propiedades según sea necesario
        return imagen;
    }


    private byte[] decodeBase64(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    @Transactional
    public Producto agregarProducto(ProductoDTO productoDTO) {
        // Verificar si ya existe un producto con el mismo nombre
        Optional<Producto> productoExistente = productoRepository.findByNombre(productoDTO.getNombre());
        if (productoExistente.isPresent()) {
            throw new RuntimeException("Ya existe un producto con el mismo nombre");
        }

        try {
            Producto producto = new Producto();

            // Generar un código aleatorio
            String codigoAleatorio = UUID.randomUUID().toString().substring(0, 10);
            producto.setCodigo(codigoAleatorio);

            producto.setNombre(productoDTO.getNombre());
            producto.setDescripcion(productoDTO.getDescripcion());

            // Recuperar y asignar las entidades Talle correspondientes a los códigos proporcionados.
            List<Talle> talles = talleRepository.findAllByCodigoIn(productoDTO.getTalles());
            producto.setTalles(new HashSet<>(talles));

            // Recuperar y asignar las entidades Color correspondientes a los códigos proporcionados.
            List<Color> colores = colorRepository.findAllByCodigoIn(productoDTO.getColores());
            producto.setColores(new HashSet<>(colores));

            // Recuperar y asignar las entidades Categoría correspondientes a los códigos proporcionados.
            List<Categoria> categorias = categoriaRepository.findAllByCodigoIn(productoDTO.getCategorias());
            producto.setCategorias(new HashSet<>(categorias));

            // Crear y guardar las imágenes asociadas al producto
            //List<ImagenDTO> imagenesDTO = (List<ImagenDTO>) productoDTO.getImagenes();
            List<ImagenDTO> imagenesDTO = new ArrayList<>(productoDTO.getImagenes());

            List<Imagen> imagenes = imagenesDTO.stream().map(this::convertirDTOaEntidad).collect(Collectors.toList());
            imagenRepository.saveAll(imagenes);

            // Asignar las imágenes al producto
            producto.setImagenes(new HashSet<>(imagenes));

            // Recuperar y asignar las entidades Característica correspondientes a los códigos proporcionados.
            List<Caracteristica> caracteristicas = caracteristicaRepository.findAllByCodigoIn(productoDTO.getCaracteristicas());
            producto.setCaracteristicas(new HashSet<>(caracteristicas));

            // Guardar el producto en la base de datos.
            return productoRepository.save(producto);
        } catch (DataIntegrityViolationException e) {
            // Capturar la excepción de violación de integridad (nombre duplicado) y devolver un mensaje.
            throw new RuntimeException("Nombre duplicado. No se pudo agregar el producto.", e);
        }
    }




    @Override
    @Transactional
    public Producto agregarProductoData(ProductoDTO productoDTO, MultipartFile imagen) {

        Optional<Producto> productoExistente = productoRepository.findByNombre(productoDTO.getNombre());
        if (productoExistente.isPresent()) {
            throw new RuntimeException("Ya existe un producto con el mismo nombre");
        }

        try {
            Producto producto = new Producto();

            String codigoAleatorio = UUID.randomUUID().toString().substring(0, 10);
            producto.setCodigo(codigoAleatorio);

            producto.setNombre(productoDTO.getNombre());
            producto.setDescripcion(productoDTO.getDescripcion());

            List<Talle> talles = talleRepository.findAllByCodigoIn(productoDTO.getTalles());
            producto.setTalles(new HashSet<>(talles));

            List<Color> colores = colorRepository.findAllByCodigoIn(productoDTO.getColores());
            producto.setColores(new HashSet<>(colores));

            List<Categoria> categorias = categoriaRepository.findAllByCodigoIn(productoDTO.getCategorias());
            producto.setCategorias(new HashSet<>(categorias));


            if (imagen != null && !imagen.isEmpty()) {
                Imagen imagenEntity = saveImagen(imagen);

                producto.getImagenes().add(imagenEntity);

                producto = productoRepository.save(producto);
            }


            List<Caracteristica> caracteristicas = caracteristicaRepository.findAllByCodigoIn(productoDTO.getCaracteristicas());
            producto.setCaracteristicas(new HashSet<>(caracteristicas));

            // Guardar el producto en la base de datos.
            return productoRepository.save(producto);
        } catch (DataIntegrityViolationException e) {

            throw new RuntimeException("Nombre duplicado. No se pudo agregar el producto.", e);
        }
    }

    private Producto mapProductoDTOToEntity(ProductoDTO productoDTO) {
        Producto producto = new Producto();

        BeanUtils.copyProperties(productoDTO, producto);

        return producto;
    }


    private Imagen saveImagen(MultipartFile imagen) {
        try {
            Imagen imagenEntity = new Imagen();

            byte[] bytes = IOUtils.toByteArray(imagen.getInputStream());

            imagenEntity.setBase64Imagen(Base64.getEncoder().encodeToString(bytes));

            return imagenRepository.save(imagenEntity);
        } catch (IOException e) {

            throw new RuntimeException("Error al procesar la imagen.", e);
        }
    }


    //(producto, color, talle, dateStart, dateEnd)
    public List<Producto> getProductosNoReservados(String fecha1, String fecha2) {
        return productoRepository.findProductosNotReserved(fecha1, fecha2);
    }


    public List<Producto> getProductosNoReservadosTotal(String producto, String color, String talle, String fecha1, String fecha2) {
        return productoRepository.findProductosNotReservedTotal(producto, color, talle, fecha1, fecha2);
    }

    /*
    public List<Producto> getProductosNoReservadosTotal(String fecha, String otraFecha, String codigoGenero, String codigoCaracteristica, String codigoColor, String codigoCategoria, String codigoTalle) {
        return productoRepository.findProductosNotReservedTotal(fecha, otraFecha, codigoGenero, codigoCaracteristica, codigoColor, codigoCategoria, codigoTalle);
    }
     */

    @Transactional
    public List<Producto> buscarProductosDisponibles(FiltroBusqueda filtro) {
        log.info("Iniciando búsqueda de productos con filtro: {}", filtro);

        try {
            List<Producto> productos = productoRepository.busqueda_productos_disponibles(
                    filtro.getSearchValue(),
                    filtro.getStartDate(),
                    filtro.getEndDate(),
                    filtro.getGender(),
                    filtro.getClothingType(),
                    filtro.getColors(),
                    filtro.getTalle(),
                    filtro.getSeason()
            );

            log.info("Número de productos encontrados: {}", productos.size());
            return productos;
        } catch (Exception e) {
            log.error("Error en la búsqueda de productos: {}", e.getMessage());
            throw e;
        }
    }



}
