package ru.isshepelev.auto.security.service;

import org.springframework.http.ResponseEntity;
import ru.isshepelev.auto.security.dto.JwtAuthenticationResponseDto;
import ru.isshepelev.auto.security.dto.RefreshTokenRequestDto;
import ru.isshepelev.auto.security.dto.SignInRequestDto;
import ru.isshepelev.auto.security.dto.SignUpRequestDto;

public interface AuthenticationService {

    ResponseEntity<?> singUp(SignUpRequestDto singUpRequest);

    ResponseEntity<?> signIn(SignInRequestDto signInRequestDto);

    JwtAuthenticationResponseDto refreshToken(RefreshTokenRequestDto request);
}
