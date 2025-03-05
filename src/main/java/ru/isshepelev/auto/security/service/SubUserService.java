package ru.isshepelev.auto.security.service;

import ru.isshepelev.auto.security.entity.Role;
import ru.isshepelev.auto.security.entity.SubUser;
import ru.isshepelev.auto.security.entity.User;
import ru.isshepelev.auto.ui.dto.CreateSubUserDto;

import java.util.Set;

public interface SubUserService {

    SubUser getSubUserByUsername(String username);

    boolean createSubUser(CreateSubUserDto createSubUserDto, String ownerUsername);
}
