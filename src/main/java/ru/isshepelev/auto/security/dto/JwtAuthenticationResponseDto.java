package ru.isshepelev.auto.security.dto;

import lombok.Data;

@Data
public class JwtAuthenticationResponseDto {
    private String token;
    private String refreshToken;
}
