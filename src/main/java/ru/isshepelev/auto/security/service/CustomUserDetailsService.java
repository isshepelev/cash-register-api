package ru.isshepelev.auto.security.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.security.entity.SubUser;
import ru.isshepelev.auto.security.entity.User;
import ru.isshepelev.auto.security.repository.SubUserRepository;
import ru.isshepelev.auto.security.repository.UserRepository;

import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final SubUserRepository subUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return new CustomUserDetails(user);
        }

        SubUser subUser = subUserRepository.findByUsername(username);
        if (subUser != null) {
            return new CustomUserDetails(subUser);
        }

        throw new UsernameNotFoundException("User not found");
    }
}
