package com.proyectoDH.service;

import com.proyectoDH.dto.ProductoDTO;
import com.proyectoDH.entities.Producto;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface IProductoService {

    public List<Producto> obtenerTodosLosProductos();

    Producto save(Producto producto);

    @Procedure(name = "eliminarProducto")
    void eliminarProducto(String codigo);

    public List<Producto> getProductosNoReservadosTotal(String producto, String color, String talle, String fecha1, String fecha2);

    public List<Producto> getProductosNoReservados(String fecha1, String fecha2);

    public Producto agregarProducto(ProductoDTO productoDTO);

    public Producto agregarProductoData(ProductoDTO productoDTO, MultipartFile imagen);
}
