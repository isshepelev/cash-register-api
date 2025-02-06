package ru.isshepelev.auto.security.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.security.repository.UserRepository;
import ru.isshepelev.auto.security.service.UserService;

@Data
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("пользователь не найден"));
            }
        };
    }
}
