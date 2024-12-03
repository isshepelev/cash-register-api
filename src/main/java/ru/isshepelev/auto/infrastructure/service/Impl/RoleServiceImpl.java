package ru.isshepelev.auto.infrastructure.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.infrastructure.persistance.entity.Role;
import ru.isshepelev.auto.infrastructure.persistance.repository.RoleRepository;
import ru.isshepelev.auto.infrastructure.service.RoleService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public void addRole(String newRole) {
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setRole(newRole);
        roleRepository.save(role);
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> getRoleById(UUID id) {
        return Optional.of(roleRepository.findById(id).get());
    }

    @Override
    public void deletRoleById(UUID id) {
        roleRepository.deleteById(id);
    }

}
