package ru.isshepelev.auto.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.isshepelev.auto.security.entity.SubUser;
import ru.isshepelev.auto.security.entity.User;

@Repository
public interface SubUserRepository extends JpaRepository<SubUser, Long> {
    SubUser findByUsername(String username);
}
