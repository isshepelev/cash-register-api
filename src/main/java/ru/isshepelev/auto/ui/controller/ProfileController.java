package ru.isshepelev.auto.ui.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.isshepelev.auto.security.entity.User;
import ru.isshepelev.auto.security.service.UserService;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;


    @GetMapping("")
    public String myProfile(Model model) {
        model.addAttribute("username", userService.getUsernameAuthorizedUser());
        User user = userService.getUserByUsername(userService.getUsernameAuthorizedUser());
        model.addAttribute("roles", user.getRoles().stream()
                .map(role -> role.getName()).collect(Collectors.joining(", ")));

        if (user.getLicense() == null) {
            model.addAttribute("licenseEndDate", null);
            model.addAttribute("licenseActive", false);
        } else {
            model.addAttribute("licenseEndDate", user.getLicense().getEndDate());
            model.addAttribute("licenseActive", user.hasValidLicense());
        }
        return "profile";
    }

    @PostMapping("/buy-license")
    public String buyLicense(@RequestParam("period") String period) {
        userService.buyLicense(period);
        return "redirect:/profile";
    }

}
