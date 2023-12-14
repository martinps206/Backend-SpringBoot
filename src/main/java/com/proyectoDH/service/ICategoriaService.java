package com.proyectoDH.service;

import com.proyectoDH.entities.Caracteristica;
import com.proyectoDH.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICategoriaService {

    List<Categoria> getAllCategorias();
    Categoria agregarCategoria(Categoria nuevaCategoria);

    Categoria actualizarCategoria(String codigo, Categoria categoria);

    void eliminarCategoria(String codigo);
}
