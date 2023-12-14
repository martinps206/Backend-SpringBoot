package com.proyectoDH.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriaDTO {

    private String codigo;
    private String nombre;

    public CategoriaDTO(String codigo, String nombre, String descripcion, String foto) {
    }
}
