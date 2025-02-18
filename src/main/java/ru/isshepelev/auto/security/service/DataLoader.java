package ru.isshepelev.auto.security.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.isshepelev.auto.infrastructure.persistance.entity.Role;
import ru.isshepelev.auto.infrastructure.persistance.repository.RoleRepository;
import ru.isshepelev.auto.security.entity.RoleUser;
import ru.isshepelev.auto.security.entity.User;
import ru.isshepelev.auto.security.repository.RoleUserRepository;
import ru.isshepelev.auto.security.repository.UserRepository;

import java.util.Set;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleUserRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        RoleUser userRole = new RoleUser();
        userRole.setName("ROLE_USER");
        roleRepository.save(userRole);

        RoleUser adminRole = new RoleUser();
        adminRole.setName("ROLE_ADMIN");
        roleRepository.save(adminRole);

//        User admin = new User();
//        admin.setUsername("admin");
//        admin.setPassword(passwordEncoder.encode("admin"));
//        admin.setRoles(Set.of(adminRole));
//        userRepository.save(admin);
    }
}