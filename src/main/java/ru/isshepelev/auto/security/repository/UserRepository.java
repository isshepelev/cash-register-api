package ru.isshepelev.auto.security.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.isshepelev.auto.security.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query(value =
            "SELECT u FROM User u " +
                    "JOIN u.license l " +
                    "WHERE l.endDate > :currentDate", nativeQuery = true)
    List<User> findUsersWithValidLicenses(@Param("currentDate") LocalDateTime currentDate);

    @Query(value =
            "SELECT u.id, u.username, l.startDate, l.endDate FROM users u " +
                    "JOIN license l ON u.license_id = l.id " +
                    "WHERE u.username = :username", nativeQuery = true)
    List<Object[]> findUserLicenseDetails(@Param("username") String username);
}
