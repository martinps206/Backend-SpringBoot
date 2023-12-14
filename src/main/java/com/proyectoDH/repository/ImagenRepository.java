package com.proyectoDH.repository;

import com.proyectoDH.dto.ImagenDTO;
import com.proyectoDH.entities.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, String> {

    List<Imagen> findAllByCodigoIn(Set<ImagenDTO> codigos);

}