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

        User user1 = new User();
        User user2 = new User();
        User user3 = new User();



        user1.setEmail("1@1");
        user1.setPassword(passwordEncoder.encode("1"));
        user1.setUsername("1");
        user1.setRoles(Set.of(adminRole));

        user2.setEmail("2@2");
        user2.setPassword(passwordEncoder.encode("2"));
        user2.setUsername("2");
        user2.setRoles(Set.of(adminRole));


        user3.setEmail("3@3");
        user3.setPassword(passwordEncoder.encode("3"));
        user3.setUsername("3");
        user3.setRoles(Set.of(adminRole));

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }
}