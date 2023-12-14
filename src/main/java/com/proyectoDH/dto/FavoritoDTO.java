package com.proyectoDH.dto;

import com.proyectoDH.entities.Producto;
import com.proyectoDH.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoritoDTO {

    private Long codigo;
    private User user;
    private Producto producto;
}
