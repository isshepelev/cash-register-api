package ru.isshepelev.auto.security.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ru.isshepelev.auto.security.service.AuthenticationService;
import ru.isshepelev.auto.security.service.UserService;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final LicenseRepository licenseRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtCoreImpl jwtCoreImpl;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> singUp(SignUpRequestDto singUpRequest){
        if (userRepository.existsByUsername((singUpRequest.getUsername()))) {
            return ResponseEntity.badRequest().body("Имя пользователя уже существует.");
        }
        if (userRepository.existsByEmail((singUpRequest.getEmail()))) {
            return ResponseEntity.badRequest().body("Email уже используется.");
        }

        License license = new License();
        license.setActive(false);
        license.setDateOfCreation(null);
        license.setDateOfExpiration(null);

        User user = new User();
        license.setUser(user);
        user.setUsername(singUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(singUpRequest.getPassword()));
        user.setEmail(singUpRequest.getEmail());
        user.setRole(Role.OWNER);
        user.setLicense(license);
        licenseRepository.save(license);
        log.info("создание новой лицензии "+ license +" для пользователя " + user);
        userRepository.save(user);
        log.info("создание нового пользователя " + user);
        return ResponseEntity.ok("success");
    }

    @Override
    public ResponseEntity<?> signIn(SignInRequestDto signInRequestDto){
        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequestDto.getUsername(), signInRequestDto.getPassword()));
            var user = userRepository.findUserByUsername(signInRequestDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("user not found"));

            var jwt = jwtCoreImpl.generateToken(user);
            var refreshToken = jwtCoreImpl.generateRefreshToken(new HashMap<>(), user);

            JwtAuthenticationResponseDto jwtAuthenticationResponseDto = new JwtAuthenticationResponseDto();
            jwtAuthenticationResponseDto.setToken(jwt);
            jwtAuthenticationResponseDto.setRefreshToken(refreshToken);
            return ResponseEntity.ok(jwtAuthenticationResponseDto);
        }catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль");
        }
    }

    @Override
    public JwtAuthenticationResponseDto refreshToken(RefreshTokenRequestDto request){
        String username = jwtCoreImpl.extractUsername(request.getToken());
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        if (!jwtCoreImpl.isTokenValid(request.getToken(), user)){
            throw new BadCredentialsException("токен не валидный");
        }
        var jwt = jwtCoreImpl.generateToken(user);

        JwtAuthenticationResponseDto jwtAuthenticationResponseDto = new JwtAuthenticationResponseDto();
        jwtAuthenticationResponseDto.setToken(jwt);
        jwtAuthenticationResponseDto.setRefreshToken(request.getToken());
        return jwtAuthenticationResponseDto;
    }

}
