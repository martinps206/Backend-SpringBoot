package com.proyectoDH.service;

import com.proyectoDH.config.ResourceNotFoundException;
import com.proyectoDH.entities.Caracteristica;
import com.proyectoDH.entities.Categoria;
import com.proyectoDH.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService implements ICategoriaService {


    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria agregarCategoria(Categoria nuevaCategoria) {

        return categoriaRepository.save(nuevaCategoria);
    }

    public void eliminarCategoria(String codigo) {
        Categoria categoria = categoriaRepository.findById(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con el código: " + codigo));

        categoriaRepository.delete(categoria);
    }

    public Categoria actualizarCategoria(String codigo, Categoria categoriaActualizada) {
        Categoria categoria = categoriaRepository.findById(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con el código: " + codigo));

        categoria.setNombre(categoriaActualizada.getNombre());

        return categoriaRepository.save(categoria);
    }
}
