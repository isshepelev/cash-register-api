package ru.isshepelev.auto.infrastructure.persistance.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.stereotype.Repository;
import ru.isshepelev.auto.infrastructure.persistance.entity.MenuRevision;
import ru.isshepelev.auto.security.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRevisionRepository extends JpaRepository<MenuRevision, Long> {
    MenuRevision findTopByOrderByDateOfCreateDesc();

    List<MenuRevision> findByOwnerUsername(String username);
    Optional<MenuRevision> findByIdAndOwnerUsername(Long id, String username);

}
