package ru.isshepelev.auto.security.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.security.entity.User;
import ru.isshepelev.auto.security.repository.UserRepository;
import ru.isshepelev.auto.security.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;



    @Override
    public String getUsernameAuthorizedUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username == null){
            log.warn("Ошибка при получении username авторизированного пользователя " + username);
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return username;
    }

    @Override
    public User getUserByUsername(String username){
        User user = userRepository.findByUsername(username);
        if (user == null){
            log.warn("Пользователь не найден " + username);
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return user;
    }
}

