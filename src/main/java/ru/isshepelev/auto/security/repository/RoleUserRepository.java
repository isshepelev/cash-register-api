package ru.isshepelev.auto.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.isshepelev.auto.security.entity.RoleUser;

@Repository
public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {
}
