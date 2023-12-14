package com.proyectoDH.service;

import com.proyectoDH.config.ResourceNotFoundException;
import com.proyectoDH.dto.CaracteristicaDTO;
import com.proyectoDH.entities.Caracteristica;
import com.proyectoDH.repository.CaracteristicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CaracteristicaService implements ICaracteristicaService{

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    public List<Caracteristica> getAllCaracteristicas() {
        return caracteristicaRepository.findAll();
    }

    public Caracteristica actualizarCaracteristica(String codigo, Caracteristica caracteristicaActualizada) {
        Caracteristica caracteristica = caracteristicaRepository.findById(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Característica no encontrada con el código: " + codigo));

        caracteristica.setNombre(caracteristicaActualizada.getNombre());
        caracteristica.setIcono(caracteristicaActualizada.getIcono());

        return caracteristicaRepository.save(caracteristica);
    }

    public void eliminarCaracteristica(String codigo) {
        Caracteristica caracteristica = caracteristicaRepository.findById(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Característica no encontrada con el código: " + codigo));

        caracteristicaRepository.delete(caracteristica);
    }

    public Caracteristica agregarCaracteristica(CaracteristicaDTO caracteristicaDTO) {
        Caracteristica nuevaCaracteristica = new Caracteristica();

        String codigoAleatorio = UUID.randomUUID().toString().substring(0, 5);

        nuevaCaracteristica.setCodigo(codigoAleatorio);
        nuevaCaracteristica.setNombre(caracteristicaDTO.getNombre());
        nuevaCaracteristica.setIcono(caracteristicaDTO.getIcono());

        return caracteristicaRepository.save(nuevaCaracteristica);
    }
}
