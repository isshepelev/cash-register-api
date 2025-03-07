package ru.isshepelev.auto;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.isshepelev.auto.security.entity.License;
import ru.isshepelev.auto.security.entity.Role;
import ru.isshepelev.auto.security.entity.User;
import ru.isshepelev.auto.security.repository.LicenseRepository;
import ru.isshepelev.auto.security.repository.RoleRepository;
import ru.isshepelev.auto.security.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Set;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final LicenseRepository licenseRepository;

    @Override
    public void run(String... args) throws Exception {
        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        roleRepository.save(userRole);

        Role managerRole = new Role();
        managerRole.setName("ROLE_MANAGER");
        roleRepository.save(managerRole);

        Role bookkeeperRole = new Role();
        bookkeeperRole.setName("ROLE_BOOKKEEPER");
        roleRepository.save(bookkeeperRole);

        Role adminRole = new Role();
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

        License license = new License();
        license.setType(License.LicenseType.MONTHLY);
        license.setStartDate(LocalDateTime.now());
        license.setEndDate(LocalDateTime.now().plusMonths(1));
        license.setUser(user1);
        licenseRepository.save(license);
        user1.setLicense(license);
        userRepository.save(user1);
    }
}