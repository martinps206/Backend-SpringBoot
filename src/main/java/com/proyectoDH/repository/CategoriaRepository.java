package com.proyectoDH.repository;

import com.proyectoDH.entities.Categoria;
import com.proyectoDH.entities.Talle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, String> {

    List<Categoria> findAllByCodigoIn(Set<String> codigos);

    //List<Categoria> findAll();

}
