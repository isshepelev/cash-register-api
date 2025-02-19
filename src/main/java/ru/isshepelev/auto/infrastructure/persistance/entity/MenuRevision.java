package ru.isshepelev.auto.infrastructure.persistance.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.isshepelev.auto.security.entity.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
@Data
@Table(name = "menu_revision")
@Entity
public class MenuRevision implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate dateOfCreate;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Menu> revision;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;


    //TODO сделать на главную страницу адекватный вывод товаров
}
