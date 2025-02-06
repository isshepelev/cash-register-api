package ru.isshepelev.auto.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@Entity
@ToString(exclude = "user")
public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isActive;
    private Date dateOfCreation;
    private Date dateOfExpiration;

    @OneToOne(mappedBy = "license", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private User user;

}
