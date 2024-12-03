package ru.isshepelev.auto.infrastructure.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class Role {
    @Id
    private UUID id;
    private String role;

}
