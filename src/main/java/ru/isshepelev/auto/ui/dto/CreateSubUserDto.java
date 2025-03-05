package ru.isshepelev.auto.ui.dto;

import lombok.Data;
import ru.isshepelev.auto.security.entity.Role;

@Data
public class CreateSubUserDto {

    private String username;
    private String password;
    private String email;
    private Role role;

}
