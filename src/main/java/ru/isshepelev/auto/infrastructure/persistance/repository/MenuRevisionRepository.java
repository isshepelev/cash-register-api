package ru.isshepelev.auto.infrastructure.persistance.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.stereotype.Repository;
import ru.isshepelev.auto.infrastructure.persistance.entity.MenuRevision;
import ru.isshepelev.auto.security.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRevisionRepository extends JpaRepository<MenuRevision, Long> {
    MenuRevision findTopByOrderByDateOfCreateDesc();

    List<MenuRevision> findByOwnerUsername(String username);
    Optional<MenuRevision> findByIdAndOwnerUsername(Long id, String username);

    @Query(value =
            "SELECT * FROM menu_revision " +
                    "WHERE user_id = " +
                    "(SELECT id FROM users WHERE username = :username) " +
                    "ORDER BY date_of_create " +
                    "DESC LIMIT 1", nativeQuery = true)
    MenuRevision findLatestMenuRevisionByUserUsername(@Param("username") String username);

    @Query(value =
            "SELECT * FROM menu_revision " +
                    "WHERE user_id = " +
                    "(SELECT id FROM users WHERE username = :username) AND date_of_create >= :startDate AND date_of_create <= :endDate", nativeQuery = true)
    List<MenuRevision> findMenuRevisionsByDateRange(@Param("username") String username, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
