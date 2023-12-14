package com.proyectoDH.repository;

import com.proyectoDH.entities.ProductoReserva;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoReservaRepository extends JpaRepository<ProductoReserva, Long> {

    List<ProductoReserva> findByIdUsuario(Long idUsuario);

    @Query("SELECT p FROM ProductoReserva p WHERE p.codigoProducto = :codigoProducto")
    List<ProductoReserva> findByIdProducto(@Param("codigoProducto") String codigoProducto);

    @Autowired
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Transactional
    @Procedure("producto_reserva_alta")
    void productoReservaAlta(
            String codiProducto,
            String codiUsuario,
            String fechaDesdeBus,
            String fechaHastaBus,
            String codiTalle,
            String codiColor);

    @Transactional
    @Procedure("producto_reserva_usuario_consul")
    List<ProductoReserva> productoReservaUsuarioConsul(@Param("codi_usuario") Long codiUsuario);



}
