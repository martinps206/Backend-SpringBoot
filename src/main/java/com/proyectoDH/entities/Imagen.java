package com.proyectoDH.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "codigo")
public class Imagen {

    @Id
    @Column(name = "cod_imagen", nullable = false, updatable = false)
    private String codigo;

    @Column(name = "descrip_imagen", nullable = false)
    private String descripcion;

    @Lob
    @Column(name = "base64imagen", nullable = false, columnDefinition = "LONGTEXT")  // Puedes usar "LONGTEXT" si es necesario
    private String base64Imagen;

    @ManyToMany(mappedBy = "imagenes")
    @JsonManagedReference
    private Set<Producto> productos = new HashSet<>();

    public void setImagenData(byte[] imagenData) {
        this.base64Imagen = Arrays.toString(imagenData);
    }
}