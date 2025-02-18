package ru.isshepelev.auto.security.dto;

import lombok.Data;

@Data
public class SignInDto {
    private String username;
    private String password;
}
