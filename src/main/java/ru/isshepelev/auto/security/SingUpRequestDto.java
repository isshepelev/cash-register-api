package ru.isshepelev.auto.security;

import lombok.Data;

@Data
public class SingUpRequestDto {
    private String username;
    private String email;
    private String password;
}
