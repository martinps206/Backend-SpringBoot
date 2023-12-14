package com.proyectoDH.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
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

@NamedStoredProcedureQuery(
        name = "eliminarProducto",
        procedureName = "eliminar_producto",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "codigo")
        }
)

@Table(name = "producto")
public class Producto {

    @Id
    @Column(name = "codigo", nullable = false, updatable = false)
    private String codigo;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String descripcion;

    @ManyToMany
    @JoinTable(
            name = "producto_talle",
            joinColumns = @JoinColumn(name = "producto_codigo"),
            inverseJoinColumns = @JoinColumn(name = "talle_codigo")
    )
    @JsonManagedReference
    private Set<Talle> talles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "producto_color",
            joinColumns = @JoinColumn(name = "producto_codigo"),
            inverseJoinColumns = @JoinColumn(name = "color_codigo")
    )
    @JsonManagedReference
    private Set<Color> colores = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "producto_categoria",
            joinColumns = @JoinColumn(name = "producto_codigo"),
            inverseJoinColumns = @JoinColumn(name = "categoria_codigo")
    )
    @JsonManagedReference
    private Set<Categoria> categorias = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "producto_imagen",
            joinColumns = @JoinColumn(name = "producto_codigo"),
            inverseJoinColumns = @JoinColumn(name = "imagen_codigo")
    )
    @JsonManagedReference
    private Set<Imagen> imagenes = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "producto_caracteristica",
            joinColumns = @JoinColumn(name = "producto_codigo"),
            inverseJoinColumns = @JoinColumn(name = "caracteristica_codigo")
    )
    @JsonManagedReference
    private Set<Caracteristica> caracteristicas = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "producto_genero",
            joinColumns = @JoinColumn(name = "producto_codigo"),
            inverseJoinColumns = @JoinColumn(name = "genero_codigo")
    )
    @JsonManagedReference
    private Set<Genero> generos = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "producto_user",
            joinColumns = @JoinColumn(name = "producto_codigo"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonManagedReference
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Favoritos> favoritos = new HashSet<>();


    public Producto(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }


    public Producto(String nombre, String descripcion, Set<Talle> talles) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.talles = talles;
    }

    public Producto(String s) {
    }

}
