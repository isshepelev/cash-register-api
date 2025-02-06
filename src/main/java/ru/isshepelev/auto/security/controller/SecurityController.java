package ru.isshepelev.auto.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.isshepelev.auto.security.dto.RefreshTokenRequestDto;
import ru.isshepelev.auto.security.dto.SignInRequestDto;
import ru.isshepelev.auto.security.dto.SignUpRequestDto;
import ru.isshepelev.auto.security.service.impl.AuthenticationService;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class SecurityController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    ResponseEntity<?> singUp(@RequestBody SignUpRequestDto singUpRequest){
        return ResponseEntity.ok(authenticationService.singUp(singUpRequest));
    }

    @PostMapping("/sign-in")
    ResponseEntity<?> signIn(@RequestBody SignInRequestDto signInRequestDto){
        return ResponseEntity.ok(authenticationService.signIn(signInRequestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequestDto request){
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }

}
