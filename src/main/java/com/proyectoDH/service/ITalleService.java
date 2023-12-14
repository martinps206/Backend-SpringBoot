package com.proyectoDH.service;

import com.proyectoDH.dto.TalleDTO;
import com.proyectoDH.entities.Talle;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ITalleService {

    public Talle createTalle(Talle talle);

    public List<TalleDTO> getAllTalles();

}
