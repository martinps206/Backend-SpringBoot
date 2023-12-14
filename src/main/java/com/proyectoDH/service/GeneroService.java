package com.proyectoDH.service;

import com.proyectoDH.config.ResourceNotFoundException;
import com.proyectoDH.dto.GeneroDTO;
import com.proyectoDH.entities.Genero;
import com.proyectoDH.repository.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeneroService {

    @Autowired
    private GeneroRepository generoRepository;

    public List<GeneroDTO> obtenerTodosLosGeneros() {
        List<Genero> generos = generoRepository.findAll();

        List<GeneroDTO> generosDTO = generos.stream()
                .map(this::convertirGeneroAEntidad)
                .collect(Collectors.toList());

        return generosDTO;
    }

    private GeneroDTO convertirGeneroAEntidad(Genero genero) {
        GeneroDTO generoDTO = new GeneroDTO();
        generoDTO.setCodigo(genero.getCodigo());
        generoDTO.setDescripcion(genero.getDescripcion());

        return generoDTO;
    }


    public Genero obtenerGeneroPorCodigo(String codigo) {
        return generoRepository.findById(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Genero no encontrado con el código: " + codigo));
    }


}

