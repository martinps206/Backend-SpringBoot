package com.proyectoDH.service;

import com.proyectoDH.entities.ProductoReserva;
import com.proyectoDH.repository.ProductoReservaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoReservaService {

    private final ProductoReservaRepository productoReservaRepository;

    @Autowired
    public ProductoReservaService(ProductoReservaRepository productoReservaRepository) {
        this.productoReservaRepository = productoReservaRepository;
    }

    public List<ProductoReserva> obtenerPorIdProducto(String codigoProducto) {
        return productoReservaRepository.findByIdProducto(codigoProducto);
    }


    public void altaProductoReserva(
            String codiProducto,
            String codiUsuario,
            String fechaDesdeBus,
            String fechaHastaBus,
            String codiTalle,
            String codiColor) {

        productoReservaRepository.productoReservaAlta(
                codiProducto,
                codiUsuario,
                fechaDesdeBus,
                fechaHastaBus,
                codiTalle,
                codiColor);
    }


    @Transactional
    public void bajaProductoReserva(Long idReserva) {
        productoReservaRepository.deleteById(idReserva);
    }

    @Transactional
    public List<ProductoReserva> consultarReservasPorUsuario(Long codiUsuario) {
        return productoReservaRepository.productoReservaUsuarioConsul(codiUsuario);
    }


}