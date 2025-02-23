package ru.isshepelev.auto.security.service;

import ru.isshepelev.auto.security.entity.User;

public interface UserService {

    String getUsernameAuthorizedUser();

    User getUserByUsername(String username);

    void buyLicense(String period);
}
