package ru.isshepelev.auto.infrastructure.service.dto;

import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import ru.isshepelev.auto.infrastructure.persistance.entity.JobTitle;

import java.util.UUID;

@Data
public class EmployeeEditDto {
    private String name;
    private String surname;
    private JobTitle jobTitle;
    private boolean isCashRegisterAccessible;
}
