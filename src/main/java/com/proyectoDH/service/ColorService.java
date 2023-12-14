package com.proyectoDH.service;

import com.proyectoDH.dto.ColorDTO;
import com.proyectoDH.entities.Color;
import com.proyectoDH.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColorService implements IColorService{

    @Autowired
    public ColorRepository colorRepository;

    public List<ColorDTO> getAllColores() {
        List<Color> colores = colorRepository.findAll();

        List<ColorDTO> coloresDTO = colores.stream()
                .map(this::convertirColorAEntidad)
                .collect(Collectors.toList());

        return coloresDTO;
    }

    private ColorDTO convertirColorAEntidad(Color color) {
        ColorDTO colorDTO = new ColorDTO();

        colorDTO.setCodigo(color.getCodigo());
        colorDTO.setDescripcion(color.getDescripcion());

        return colorDTO;
    }

}
