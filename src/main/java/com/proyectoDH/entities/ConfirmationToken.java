package com.proyectoDH.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String token;

    // getters and setters

    public ConfirmationToken(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
