package com.proyectoDH.service;

import com.proyectoDH.entities.Color;
import com.proyectoDH.entities.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface IImagenService extends JpaRepository<Imagen, String> {
}
