package ru.isshepelev.auto.security.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.security.entity.SubUser;
import ru.isshepelev.auto.security.entity.User;
import ru.isshepelev.auto.security.repository.SubUserRepository;
import ru.isshepelev.auto.security.repository.UserRepository;
import ru.isshepelev.auto.security.service.SubUserService;
import ru.isshepelev.auto.ui.dto.CreateSubUserDto;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubUserServiceImpl implements SubUserService {
    private final SubUserRepository subUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public SubUser getSubUserByUsername(String username) {
        return Optional.ofNullable(subUserRepository.findByUsername(username))
                .orElse(null);
    }

    @Override
    public boolean createSubUser(CreateSubUserDto createSubUserDto, String ownerUsername) {
        if (userRepository.findByUsername(createSubUserDto.getUsername()) != null ||
                getSubUserByUsername(createSubUserDto.getUsername()) != null) {
            return false;
        }

        User owner = userRepository.findByUsername(ownerUsername);
        if (owner == null) {
            log.error("Владелец не найден: {}", ownerUsername);
            return false;
        }

        SubUser subUser = new SubUser();
        subUser.setOwner(owner);
        subUser.setUsername(createSubUserDto.getUsername());
        subUser.setPassword(passwordEncoder.encode(createSubUserDto.getPassword()));
        subUser.setEmail(createSubUserDto.getEmail());
        subUser.getRoles().add(createSubUserDto.getRole());

        subUserRepository.save(subUser);
        log.info("Создан работник {} у пользователя {}", subUser.getUsername(), owner.getUsername());
        return true;
    }
}
