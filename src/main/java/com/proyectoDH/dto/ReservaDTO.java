package com.proyectoDH.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservaDTO {
    private String codiProducto;
    private String codiUsuario;
    private String fechaDesdeBus;
    private String fechaHastaBus;
    private String codiTalle;
    private String codiColor;

    // Getters y Setters
}

