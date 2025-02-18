package ru.isshepelev.auto.security.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "roleUser")
public class RoleUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
