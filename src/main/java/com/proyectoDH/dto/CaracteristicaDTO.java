package com.proyectoDH.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaracteristicaDTO {

    private String codigo;
    private String nombre;
    private String icono;
}
