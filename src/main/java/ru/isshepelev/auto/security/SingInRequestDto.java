package ru.isshepelev.auto.security;

import lombok.Data;

@Data
public class SingInRequestDto {
    private String username;
    private String password;
}
