package com.proyectoDH.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "producto_reserva")
public class ProductoReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    private String codigoProducto;
    private Long idUsuario;
    private String codigoTalle;
    private String codigoColor;
    private Date fechaDesde;
    private Date fechaHasta;

}
