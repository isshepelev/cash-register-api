package ru.isshepelev.auto.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.isshepelev.auto.security.entity.Role;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ProfileDto {
    private String roles;
    private List<Role> allRoles;
    private LocalDateTime licenseEndDate;
    private boolean licenseActive;
    private boolean isAdmin;
}
