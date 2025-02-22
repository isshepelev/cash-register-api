package ru.isshepelev.auto.infrastructure.service.dto;

import lombok.Data;
import ru.isshepelev.auto.infrastructure.persistance.entity.JobTitle;

@Data
public class EmployeeCreateDto {

    private String name;
    private String surname;
    private int personalCode;
    private JobTitle jobTitle;
    private boolean isCashRegisterAccessible;

}
