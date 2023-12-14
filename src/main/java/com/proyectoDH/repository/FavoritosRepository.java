package com.proyectoDH.repository;

import com.proyectoDH.entities.Color;
import com.proyectoDH.entities.Favoritos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FavoritosRepository extends JpaRepository<Favoritos, Long> {

    List<Color> findAllByCodigoIn(Set<String> codigos);

    void deleteByUserId(Long userId);

    List<Favoritos> findByUserId(Long userId);

}
