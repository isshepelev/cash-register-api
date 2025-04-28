package ru.isshepelev.auto.security.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
public class License implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private LicenseType type;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    public enum LicenseType implements Serializable{
        MONTHLY, HALF_YEAR, YEARLY;
    }

    @Override
    public String toString() {
        return "License{id=" + id + ", type=" + type + ", startDate=" + startDate + ", endDate=" + endDate + "}";
    }
}
