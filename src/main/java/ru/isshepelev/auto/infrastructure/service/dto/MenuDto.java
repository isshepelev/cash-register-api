package ru.isshepelev.auto.infrastructure.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto {
    private String name;
    private String description;
    private int count;

}
