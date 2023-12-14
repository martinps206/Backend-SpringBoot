package com.proyectoDH.repository;

import com.proyectoDH.entities.Talle;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TalleRepository extends JpaRepository<Talle, String> {

    List<Talle> findAllByCodigoIn(Set<String> codigos);

}
