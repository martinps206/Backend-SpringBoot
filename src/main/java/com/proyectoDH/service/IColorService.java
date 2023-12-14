package com.proyectoDH.service;

import com.proyectoDH.dto.ColorDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IColorService {

    public List<ColorDTO> getAllColores();
}
