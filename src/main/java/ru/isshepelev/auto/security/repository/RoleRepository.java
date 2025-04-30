package ru.isshepelev.auto.security.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.isshepelev.auto.security.entity.Role;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    @Query(value =
            "SELECT r.* FROM roleUser r " +
                    "JOIN user_roles ur ON r.id = ur.roleUser_id " +
                    "WHERE ur.user_id = :userId", nativeQuery = true)
    List<Role> findRolesByUserId(@Param("userId") Long userId);
}
