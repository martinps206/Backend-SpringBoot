package com.proyectoDH.repository;

import com.proyectoDH.entities.Genero;
import com.proyectoDH.entities.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, String> {

    List<Genero> findAllByCodigoIn(Set<String> codigos);
}
