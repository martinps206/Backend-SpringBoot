package com.proyectoDH.repository;

import com.proyectoDH.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {

    Optional<Producto> findByCodigo(String codigo);

    List<Producto> findByCodigoIn(List<String> codigos);

    @Procedure(name = "eliminar_producto")
    void eliminarProducto(String codigo);

    Optional<Producto> findByNombre(String nombre);

    @Query("SELECT p FROM Producto p " +
            "LEFT JOIN ProductoReserva pr " +
            "ON p.codigo = pr.codigoProducto " +
            "AND (:fecha BETWEEN pr.fechaDesde AND pr.fechaHasta OR :otraFecha BETWEEN pr.fechaDesde AND pr.fechaHasta) " +
            "WHERE pr.codigoProducto IS NULL")
    List<Producto> findProductosNotReserved(@Param("fecha") String fecha, @Param("otraFecha") String otraFecha);


    @Query("SELECT p FROM Producto p " +
            "LEFT JOIN ProductoReserva pr " +
            "ON p.codigo = pr.codigoProducto " +
            "AND (:fecha BETWEEN pr.fechaDesde AND pr.fechaHasta OR :otraFecha BETWEEN pr.fechaDesde AND pr.fechaHasta) " +
            "AND p.nombre = :producto " +
            "AND pr.codigoColor = :color " +
            "AND pr.codigoTalle = :talle " +
            "WHERE pr.codigoProducto IS NULL")
    List<Producto> findProductosNotReservedTotal(@Param("producto") String producto, @Param("color") String color, @Param("talle") String talle, @Param("fecha") String fecha, @Param("otraFecha") String otraFecha);


    /*
    @Query("SELECT p FROM Producto p " +
            "LEFT JOIN ProductoReserva pr ON p.codigo = pr.codigoProducto " +
            "LEFT JOIN ProductoColor pc ON p.codigo = pc.productoCodigo " +
            "LEFT JOIN ProductoTalle pt ON p.codigo = pt.productoCodigo " +
            "LEFT JOIN ProductoGenero pg ON p.codigo = pg.productoCodigo " +
            "LEFT JOIN ProductoCaracteristica pcr ON p.codigo = pcr.productoCodigo " +
            "LEFT JOIN ProductoCategoria pct ON p.codigo = pct.productoCodigo " +
            "WHERE pr.codigoProducto IS NULL " +
            "AND (:fecha BETWEEN pr.fechaDesde AND pr.fechaHasta OR :otraFecha BETWEEN pr.fechaDesde AND pr.fechaHasta) " +
            "AND (:codigoGenero IS NULL OR pg.generoCodigo = :codigoGenero) " +
            "AND (:codigoCaracteristica IS NULL OR pcr.caracteristicaCodigo = :codigoCaracteristica) " +
            "AND (:codigoColor IS NULL OR pc.colorCodigo = :codigoColor) " +
            "AND (:codigoCategoria IS NULL OR pct.categoriaCodigo = :codigoCategoria) " +
            "AND (:codigoTalle IS NULL OR pt.talleCodigo = :codigoTalle)")
    List<Producto> findProductosNotReservedTotal(
            @Param("fecha") String fecha,
            @Param("otraFecha") String otraFecha,
            @Param("codigoGenero") String codigoGenero,
            @Param("codigoCaracteristica") String codigoCaracteristica,
            @Param("codigoColor") String codigoColor,
            @Param("codigoCategoria") String codigoCategoria,
            @Param("codigoTalle") String codigoTalle);
        */

    @Procedure(name = "busqueda_productos_disponibles")
    List<Producto> busqueda_productos_disponibles(
            @Param("searchValue") String searchValue,
            @Param("fecha_desde_bus") String fechaDesde,
            @Param("fecha_hasta_bus") String fechaHasta,
            @Param("codi_genero") String genero,
            @Param("codi_caracteristica") String caracteristica,
            @Param("codi_color") String color,
            @Param("codi_talle") String talle,
            @Param("codi_categoria") String categoria
    );


}
