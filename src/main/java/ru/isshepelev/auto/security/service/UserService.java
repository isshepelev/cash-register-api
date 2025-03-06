package ru.isshepelev.auto.security.service;

import ru.isshepelev.auto.security.dto.ProfileDto;
import ru.isshepelev.auto.security.dto.SignUpDto;
import ru.isshepelev.auto.security.entity.User;

public interface UserService {

    void registerUser(SignUpDto signUpDto);

    String getUsernameAuthorizedUser();

    User getUserByUsername(String username);

    ProfileDto getProfileInfo(String username);

    void buyLicense(String period);
}
