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

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "codigo")
public class Talle {

    @Id
    @Column(name = "cod_talle",nullable = false, updatable = false)
    private String codigo;
    @Column(name = "descrip_talle", nullable = false)
    private String descripcion;

    @ManyToMany(mappedBy = "talles")
    @JsonManagedReference
    private Set<Producto> productos = new HashSet<>();

}
