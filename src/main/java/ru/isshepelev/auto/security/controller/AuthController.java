package ru.isshepelev.auto.security.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.isshepelev.auto.infrastructure.exception.UsernameAlreadyExistsException;
import ru.isshepelev.auto.security.dto.SignInDto;
import ru.isshepelev.auto.security.dto.SignUpDto;
import ru.isshepelev.auto.security.entity.User;
import ru.isshepelev.auto.security.repository.UserRepository;
import ru.isshepelev.auto.security.service.SubUserService;
import ru.isshepelev.auto.security.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@AllArgsConstructor
@Slf4j
public class AuthController {
    private final SubUserService subUserService;
    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute SignUpDto signUpDto, Model model) {
        try {
            userService.registerUser(signUpDto);
        } catch (UsernameAlreadyExistsException e) {
            model.addAttribute("error", "Пользователь уже существует");
            return "register";
        }
        return "redirect:/login";
    }


    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }


}
