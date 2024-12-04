package ru.isshepelev.auto.infrastructure.persistance.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Entity
@Data
@Table(name = "menu")
public class Menu {

    @Id
    private UUID id;

    private String name;
    private String description;
    private int count;
    @PrePersist
    @PreUpdate
    private void ensureNonNegativeCount() {
        if (this.count < 0) {
            this.count = 0;
        }
    }

}
