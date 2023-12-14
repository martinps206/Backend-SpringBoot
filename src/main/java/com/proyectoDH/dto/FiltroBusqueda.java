package com.proyectoDH.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiltroBusqueda {
    private String searchValue;
    private String startDate;
    private String endDate;
    private String gender;
    private String clothingType;
    private String colors;
    private String talle;
    private String season;
}
