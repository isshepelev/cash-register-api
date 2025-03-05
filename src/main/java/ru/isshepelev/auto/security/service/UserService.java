package ru.isshepelev.auto.security.service;

import ru.isshepelev.auto.security.dto.ProfileDto;
import ru.isshepelev.auto.security.entity.User;

public interface UserService {

    String getUsernameAuthorizedUser();

    User getUserByUsername(String username);

    ProfileDto getProfileInfo(String username);

    void buyLicense(String period);
}
