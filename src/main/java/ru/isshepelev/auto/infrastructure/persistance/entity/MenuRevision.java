package ru.isshepelev.auto.infrastructure.persistance.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.isshepelev.auto.infrastructure.persistance.entity.enums.StatusRevision;

import java.time.LocalDate;
import java.util.List;
@Data
@Table(name = "menu_revision")
@Entity
public class MenuRevision {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate dateOfCreate;
    private StatusRevision status;

    @OneToMany
    private List<Menu> revision;

}
