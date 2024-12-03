package ru.isshepelev.auto.infrastructure.service.dto;

import lombok.Data;
import ru.isshepelev.auto.infrastructure.persistance.entity.Role;

@Data
public class EmployeeCreateDto {

    private String name;
    private String surname;
    private int personalCode;
    private Role role;
    private boolean isCashRegisterAccessible;

}
