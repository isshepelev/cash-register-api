package ru.isshepelev.auto.ui.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.isshepelev.auto.security.dto.ProfileDto;
import ru.isshepelev.auto.security.service.SubUserService;
import ru.isshepelev.auto.security.service.UserService;
import ru.isshepelev.auto.ui.dto.CreateSubUserDto;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final SubUserService subUserService;

    @GetMapping("")
    public String myProfile(Model model) {
        String username = userService.getUsernameAuthorizedUser();
        model.addAttribute("username", username);

        ProfileDto profileDto = userService.getProfileInfo(username);
        model.addAttribute("roles", profileDto.getRoles());
        model.addAttribute("allRoles", profileDto.getAllRoles());
        model.addAttribute("licenseEndDate", profileDto.getLicenseEndDate());
        model.addAttribute("licenseActive", profileDto.isLicenseActive());

        return profileDto.isAdmin() ? "admin" : "profile";
    }

    @PostMapping("/buy-license")
    public String buyLicense(@RequestParam("period") String period) {
        userService.buyLicense(period);
        return "redirect:/profile";
    }

    @PostMapping("/create-subUser")
    public String createSubUser(@ModelAttribute CreateSubUserDto createSubUserDto, RedirectAttributes redirectAttributes) {
        boolean success = subUserService.createSubUser(createSubUserDto, userService.getUsernameAuthorizedUser());
        if (!success) {
            redirectAttributes.addFlashAttribute("error", "Пользователь с таким username уже существует");
        }
        return "redirect:/profile";
    }
}
