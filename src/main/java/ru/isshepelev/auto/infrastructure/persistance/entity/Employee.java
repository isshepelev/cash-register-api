package ru.isshepelev.auto.infrastructure.persistance.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Data
@Entity
public class Employee {

    @Id
    private UUID id;

    private String name;
    private String surname;
    private int personalCode;
    @OneToOne(fetch = FetchType.EAGER)
    private Role role;
    private boolean isCashRegisterAccessible;


}
