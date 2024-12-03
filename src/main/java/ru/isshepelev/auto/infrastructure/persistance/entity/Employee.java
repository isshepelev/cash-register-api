package ru.isshepelev.auto.infrastructure.persistance.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Data
@Entity
public class Employee {

    @Id
    private UUID id;

    private String name;
    private String surname;
    private int personalCode;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;
    private boolean isCashRegisterAccessible;
}
