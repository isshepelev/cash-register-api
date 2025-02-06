package ru.isshepelev.auto.security.dto;

import lombok.Data;

@Data
public class SignUpRequestDto {
    private String username;
    private String email;
    private String password;
}
