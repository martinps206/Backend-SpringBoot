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
public class Caracteristica {

    @Id
    @Column(name = "cod_caracteristica", nullable = false, updatable = false)
    private String codigo;

    @Column(name = "nombre_caracteristica", nullable = false)
    private String nombre;

    @Column(name = "icono_caracteristica", nullable = false)
    private String icono;

    @ManyToMany(mappedBy = "caracteristicas")
    @JsonIgnore
    private Set<Producto> productos = new HashSet<>();
}
