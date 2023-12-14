package com.proyectoDH.service;

import com.proyectoDH.dto.ImagenDTO;
import com.proyectoDH.entities.Imagen;
import com.proyectoDH.entities.Producto;
import com.proyectoDH.repository.ImagenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImagenService {

    private final ImagenRepository imagenRepository;

    @Autowired
    public ImagenService(ImagenRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    @Transactional
    public void guardarListaImagenes(List<ImagenDTO> imagenesDTO) {
        List<Imagen> imagenes = imagenesDTO.stream()
                .map(this::convertirDTOaEntidad)
                .collect(Collectors.toList());

        imagenRepository.saveAll(imagenes);
    }

    private Imagen convertirDTOaEntidad(ImagenDTO imagenDTO) {
        Imagen imagen = new Imagen();
        imagen.setCodigo(imagenDTO.getCodigo());
        imagen.setDescripcion(imagenDTO.getDescripcion());
        imagen.setBase64Imagen(imagenDTO.getBase64Imagen());

        return imagen;
    }

}
