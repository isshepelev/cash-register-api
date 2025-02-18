package ru.isshepelev.auto.security.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class License {

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
}
enum LicenseType {
    MONTHLY, HALF_YEAR, YEARLY;
}