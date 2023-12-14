package com.proyectoDH.service;

import com.proyectoDH.dto.CaracteristicaDTO;
import com.proyectoDH.entities.Caracteristica;
import com.proyectoDH.entities.Categoria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICaracteristicaService {

    public List<Caracteristica> getAllCaracteristicas();

    Caracteristica actualizarCaracteristica(String codigo, Caracteristica caracteristica);

    void eliminarCaracteristica(String codigo);

    public Caracteristica agregarCaracteristica(CaracteristicaDTO caracteristicaDTO);

}
