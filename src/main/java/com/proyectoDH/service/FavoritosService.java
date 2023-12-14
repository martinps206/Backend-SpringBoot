package com.proyectoDH.service;

import com.proyectoDH.dto.FavoritoDTO;
import com.proyectoDH.entities.Favoritos;
import com.proyectoDH.entities.Producto;
import com.proyectoDH.entities.User;
import com.proyectoDH.repository.FavoritosRepository;
import com.proyectoDH.repository.ProductoRepository;
import com.proyectoDH.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoritosService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private FavoritosRepository favoritosRepository;

    @Transactional
    public void agregarProductoAFavoritos(Long userId, List<String> productoCodigos) {

        System.out.println("martin user id" + userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));

        List<Producto> productos = productoRepository.findByCodigoIn(productoCodigos);

        if (productos.size() != productoCodigos.size()) {
            throw new RuntimeException("Al menos uno de los productos no fue encontrado.");
        }

        for (Producto producto : productos) {
            Favoritos favoritos = new Favoritos();
            favoritos.setUser(user);
            favoritos.setProducto(producto);
            favoritosRepository.save(favoritos);
        }
    }

    public void eliminarFavoritosPorUsuario(Long userId) {
        List<Favoritos> favoritos = favoritosRepository.findByUserId(userId);
        favoritosRepository.deleteAll(favoritos);
    }

    public List<Favoritos> obtenerFavoritosPorUsuario(Long userId) {
        return favoritosRepository.findByUserId(userId);
    }

    public List<FavoritoDTO> obtenerFavoritosDTOPorUsuario(Long userId) {
        List<Favoritos> favoritos = favoritosRepository.findByUserId(userId);
        return favoritos.stream()
                .map(this::convertirAFavoritoDTO)
                .collect(Collectors.toList());
    }

    private FavoritoDTO convertirAFavoritoDTO(Favoritos favoritos) {

        FavoritoDTO favoritoDTO = new FavoritoDTO();
        favoritoDTO.setCodigo(favoritos.getCodigo());
        favoritoDTO.setUser(favoritos.getUser());
        favoritoDTO.setProducto(favoritos.getProducto());
        return favoritoDTO;
    }


}

