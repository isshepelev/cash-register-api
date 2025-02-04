package ru.isshepelev.auto.security;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class SecurityController {
    private final UserRepository userRepository; //TODO переделать чтобы тут был сервис
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;



    @PostMapping("/singup")
    ResponseEntity<?> singUp(@RequestBody SingUpRequestDto singUpRequest){
        if (userRepository.existsUserByUsername(singUpRequest.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("имя уже занято");
        }
        if (userRepository.existsUserByEmail(singUpRequest.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("почта уже занята");
        }
        User user = new User();
        user.setUsername(singUpRequest.getUsername());
        user.setEmail(singUpRequest.getEmail());
        user.setRole(Role.ADMIN);
        user.setPassword(passwordEncoder.encode(singUpRequest.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/singin")
    ResponseEntity<?> signIn(@RequestBody SingUpRequestDto singUpRequestDto){
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(singUpRequestDto.getUsername(), singUpRequestDto.getPassword()));
        }catch (BadCredentialsException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);
        return ResponseEntity.ok(jwt);
    }

}
