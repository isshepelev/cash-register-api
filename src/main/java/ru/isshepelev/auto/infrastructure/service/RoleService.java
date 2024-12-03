package ru.isshepelev.auto.infrastructure.service;

import org.springframework.stereotype.Service;
import ru.isshepelev.auto.infrastructure.persistance.entity.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface RoleService {

    void addRole(String newRole);

    List<Role> getRoles();

    Optional<Role> getRoleById(UUID id);

    void deletRoleById(UUID id);
}
