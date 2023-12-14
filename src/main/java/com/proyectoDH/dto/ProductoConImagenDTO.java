package com.proyectoDH.dto;

import com.proyectoDH.entities.Talle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoConImagenDTO {
    private String codigo;
    private String nombre;
    private String descripcion;
    private Set<TalleDTO> talles;
    private Set<ColorDTO> colores;
    private Set<CategoriaDTO> categorias;
    private Set<ImagenDTO>  imagenes;
    private Set<CaracteristicaDTO>  caracteristicas;


    // Getters y Setters
}

