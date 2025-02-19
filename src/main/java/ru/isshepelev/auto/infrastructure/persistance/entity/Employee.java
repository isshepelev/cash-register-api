package ru.isshepelev.auto.infrastructure.persistance.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.isshepelev.auto.security.entity.User;

import java.util.UUID;

@Data
@Entity
public class Employee {

    @Id
    private UUID id;

    private String name;
    private String surname;
    private int personalCode;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;
    private boolean isCashRegisterAccessible;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;
}
