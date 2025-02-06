package ru.isshepelev.auto.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.isshepelev.auto.security.entity.Role;
import ru.isshepelev.auto.security.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);
    User findByRole(Role role);
    boolean existsUserByUsername(String username);
    boolean existsUserByEmail(String email);
}
