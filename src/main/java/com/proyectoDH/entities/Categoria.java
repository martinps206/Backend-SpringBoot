package com.proyectoDH.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "codigo")
public class Categoria {

    @Id
    @Column(name = "cod_categoria", nullable = false, updatable = false)
    private String codigo;

    @Column(name = "nombre_categoria", nullable = false)
    private String nombre;

    @Column(name = "descripcion_categoria", nullable = false)
    private String descripcion;

    @Column(name = "foto_categoria", nullable = false)
    private String foto;

    @ManyToMany(mappedBy = "categorias")
    @JsonIgnore
    private Set<Producto> productos = new HashSet<>();
}

