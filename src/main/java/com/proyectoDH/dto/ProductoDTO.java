package com.proyectoDH.dto;

import com.proyectoDH.entities.Categoria;
import com.proyectoDH.entities.Color;
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
public class ProductoDTO {

    private String codigo;
    private String nombre;
    private String descripcion;
    private Set<String> talles;
    private Set<String> colores;
    private Set<String> categorias;
    private Set<ImagenDTO>  imagenes;
    private Set<String>  caracteristicas;
    private String token;

}
