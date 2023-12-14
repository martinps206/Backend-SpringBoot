package com.proyectoDH.service;

import com.proyectoDH.dto.TalleDTO;
import com.proyectoDH.entities.Talle;
import com.proyectoDH.repository.TalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TalleService implements ITalleService {

    private final TalleRepository talleRepository;

    @Autowired
    public TalleService(TalleRepository talleRepository) {
        this.talleRepository = talleRepository;
    }

    public Talle createTalle(Talle talle) {
        return talleRepository.save(talle);
    }

    public List<TalleDTO> getAllTalles() {
        List<Talle> talles = talleRepository.findAll();


        List<TalleDTO> tallesDTO = talles.stream()
                .map(this::convertirTalleAEntidad)
                .collect(Collectors.toList());

        return tallesDTO;
    }

    private TalleDTO convertirTalleAEntidad(Talle talle) {
        TalleDTO talleDTO = new TalleDTO();

        talleDTO.setCodigo(talle.getCodigo());
        talleDTO.setDescripcion(talle.getDescripcion());

        return talleDTO;
    }
}
