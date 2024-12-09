package ru.isshepelev.auto.infrastructure.service.dto;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.UUID;

@Data
@Embeddable
public class OrderMenuDto {
    private UUID id;
    private String name;
    private String description;
}
