package ru.isshepelev.auto.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.isshepelev.auto.security.dto.*;
import ru.isshepelev.auto.security.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class SecurityController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    ResponseEntity<?> singUp(@RequestBody SignUpRequestDto singUpRequest){
        return authenticationService.singUp(singUpRequest);
    }

    @PostMapping("/sign-in")
    ResponseEntity<?> signIn(@RequestBody SignInRequestDto signInRequestDto){
        return authenticationService.signIn(signInRequestDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequestDto request){
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }

}
//TODO сделать чтобы при переходе на любой url перебрасывало на страницу с регистрацией или входом так же сделать обновление jwt токена переделать html страничку под единый стиль