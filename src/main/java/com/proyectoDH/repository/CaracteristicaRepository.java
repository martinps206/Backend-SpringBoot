package com.proyectoDH.repository;

import com.proyectoDH.entities.Caracteristica;
import com.proyectoDH.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CaracteristicaRepository extends JpaRepository<Caracteristica, String> {

    List<Caracteristica> findAllByCodigoIn(Set<String> codigos);
}
