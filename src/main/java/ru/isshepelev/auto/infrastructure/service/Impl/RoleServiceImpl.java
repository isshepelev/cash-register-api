package ru.isshepelev.auto.infrastructure.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.infrastructure.persistance.entity.Employee;
import ru.isshepelev.auto.infrastructure.persistance.entity.Role;
import ru.isshepelev.auto.infrastructure.persistance.repository.EmployeeRepository;
import ru.isshepelev.auto.infrastructure.persistance.repository.RoleRepository;
import ru.isshepelev.auto.infrastructure.service.EmployeeService;
import ru.isshepelev.auto.infrastructure.service.RoleService;
import ru.isshepelev.auto.security.entity.User;
import ru.isshepelev.auto.security.repository.UserRepository;
import ru.isshepelev.auto.security.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final UserService userService;

    @Override
    public void addRole(String newRole) {
        for (Role roles : getRoles()){
            if (roles.getRole().toLowerCase().equals(newRole.toLowerCase())){
                log.warn("Должность уже существует. Добавлена не будет");
                return;
            }
        }
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setRole(newRole);
        log.info("Добавление новой роли {}", role);
        User user = userService.getUserByUsername(userService.getUsernameAuthorizedUser());
        if (user == null){
            throw new UsernameNotFoundException("User not found");
        }
        role.setOwner(user);
        roleRepository.save(role);
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll().stream()
                .filter(role -> role.getOwner().getUsername().equals(userService.getUsernameAuthorizedUser()))
                .toList();

    }

    @Override
    public Optional<Role> getRoleById(UUID id) {
        return roleRepository.findById(id)
                .filter(role -> role.getOwner().getUsername().equals(userService.getUsernameAuthorizedUser()));
    }

    @Override
    public void deletRoleById(UUID id) {
        Role role = getRoleById(id).orElseThrow(() -> new RuntimeException("должность не найдена"));
        if (employeeRepository.existsByRole(role)){
            throw new RuntimeException("Невозможно удалить должность, так как она используется сотрудниками");
        }
        log.info("Удаление роли с id {}", id);
        roleRepository.deleteById(id);
    }

}
