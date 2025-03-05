package ru.isshepelev.auto.security.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.isshepelev.auto.security.dto.SignInDto;
import ru.isshepelev.auto.security.dto.SignUpDto;
import ru.isshepelev.auto.security.entity.User;
import ru.isshepelev.auto.security.repository.UserRepository;
import ru.isshepelev.auto.security.service.SubUserService;

@Controller
@AllArgsConstructor
@Slf4j
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubUserService subUserService;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute SignUpDto signUpDto, Model model) {
        if (userRepository.findByUsername(signUpDto.getUsername()) != null || subUserService.getSubUserByUsername(signUpDto.getUsername()) != null) {
            model.addAttribute("error", "Пользователь уже существует");
            return "register";
        }
        User user = new User();
        user.setUsername(signUpDto.getUsername());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setEmail(signUpDto.getEmail());
        userRepository.save(user);

        return "redirect:/login";
    }


    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }


}
