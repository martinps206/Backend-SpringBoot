package com.proyectoDH.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImagenDTO {
    private String codigo;
    private String descripcion;
    private String base64Imagen;

    public ImagenDTO(Long codigo, String descripcion, String base64Imagen) {
    }
}
