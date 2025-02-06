package ru.isshepelev.auto.security.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.security.dto.JwtAuthenticationResponseDto;
import ru.isshepelev.auto.security.dto.RefreshTokenRequestDto;
import ru.isshepelev.auto.security.dto.SignInRequestDto;
import ru.isshepelev.auto.security.dto.SignUpRequestDto;
import ru.isshepelev.auto.security.entity.License;
import ru.isshepelev.auto.security.entity.Role;
import ru.isshepelev.auto.security.entity.User;
import ru.isshepelev.auto.security.repository.LicenseRepository;
import ru.isshepelev.auto.security.repository.UserRepository;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository; //TODO переделать чтобы тут был сервис
    private final LicenseRepository licenseRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;

    public User singUp(SignUpRequestDto singUpRequest){ //TODO добавить проверки
        License license = new License();
        license.setActive(false);
        license.setDateOfCreation(null);
        license.setDateOfExpiration(null);

        User user = new User();
        license.setUser(user);
        user.setUsername(singUpRequest.getUsername()); // TODO проверка на та что пользователь с таким уже существует добавить надо
        user.setPassword(passwordEncoder.encode(singUpRequest.getPassword()));
        user.setEmail(singUpRequest.getEmail());
        user.setRole(Role.OWNER);
        user.setLicense(license);
        licenseRepository.save(license);
        log.info("создание новой лицензии "+ license +" для пользователя " + user);
        userRepository.save(user);
        log.info("создание нового пользователя " + user);
        return user;
    }

    public JwtAuthenticationResponseDto signIn(SignInRequestDto signInRequestDto){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequestDto.getUsername(), signInRequestDto.getPassword()));
        var user = userRepository.findUserByUsername(signInRequestDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("не верный логин или пароль"));

        var jwt = jwtCore.generateToken(user);
        var refreshToken = jwtCore.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponseDto jwtAuthenticationResponseDto = new JwtAuthenticationResponseDto();
        jwtAuthenticationResponseDto.setToken(jwt);
        jwtAuthenticationResponseDto.setRefreshToken(refreshToken);
        return jwtAuthenticationResponseDto;

    }

    public JwtAuthenticationResponseDto refreshToken(RefreshTokenRequestDto request){
        String username = jwtCore.extractUsername(request.getToken());
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("пользователь не найден"));
        if (!jwtCore.isTokenValid(request.getToken(), user)){
            throw new BadCredentialsException("токен не валидный");
        }
        var jwt = jwtCore.generateToken(user);

        JwtAuthenticationResponseDto jwtAuthenticationResponseDto = new JwtAuthenticationResponseDto();
        jwtAuthenticationResponseDto.setToken(jwt);
        jwtAuthenticationResponseDto.setRefreshToken(request.getToken());
        return jwtAuthenticationResponseDto;
    }

}
