package ru.isshepelev.auto.security.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.security.dto.ProfileDto;
import ru.isshepelev.auto.security.entity.License;
import ru.isshepelev.auto.security.entity.Role;
import ru.isshepelev.auto.security.entity.SubUser;
import ru.isshepelev.auto.security.entity.User;
import ru.isshepelev.auto.security.repository.LicenseRepository;
import ru.isshepelev.auto.security.repository.RoleRepository;
import ru.isshepelev.auto.security.repository.UserRepository;
import ru.isshepelev.auto.security.service.SubUserService;
import ru.isshepelev.auto.security.service.UserService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LicenseRepository licenseRepository;
    private final RoleRepository roleRepository;
    private final SubUserService subUserService;

    @Override
    public String getUsernameAuthorizedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username == null) {
            log.warn("Ошибка при получении username авторизированного пользователя " + username);
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return username;
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.warn("Пользователь не найден " + username);
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return user;
    }

    @Override
    public ProfileDto getProfileInfo(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return new ProfileDto(
                    user.getRoles().stream().map(Role::getName).collect(Collectors.joining(", ")),
                    roleRepository.findAll(),
                    user.getLicense() != null ? user.getLicense().getEndDate() : null,
                    user.hasValidLicense(),
                    true,
                    user.getSubUsers()
            );
        }
        SubUser subUser = subUserService.getSubUserByUsername(username);
        return new ProfileDto(
                subUser.getRoles().stream().map(Role::getName).collect(Collectors.joining(", ")),
                Collections.emptyList(),
                null,
                false,
                false,
                null
        );
    }

    @Override
    public void buyLicense(String period) {
        switch (period) {
            case "MONTHLY":
                generateLicense(Duration.ofDays(30), License.LicenseType.MONTHLY);
                break;

            case "HALF_YEAR":
                generateLicense(Duration.ofDays(180), License.LicenseType.HALF_YEAR);
                break;

            case "YEARLY":
                generateLicense(Duration.ofDays(365), License.LicenseType.YEARLY);
                break;
        }
    }

    private void generateLicense(Duration days, License.LicenseType type){
        User user = userRepository.findByUsername(getUsernameAuthorizedUser());
        if (user == null){
            log.error("user not found " + getUsernameAuthorizedUser());
            return;
        }

        if (user.hasValidLicense()){
            user.getLicense().setEndDate(user.getLicense().getEndDate().plus(days));
            user.getLicense().setType(type);
        }else {
            License license = new License();
            license.setUser(user);
            license.setType(type);
            license.setStartDate(LocalDateTime.now());
            license.setEndDate(LocalDateTime.now().plus(days));
            licenseRepository.save(license);
            user.setLicense(license);
        }
        userRepository.save(user);
    }
}

