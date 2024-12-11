package ru.isshepelev.auto.infrastructure.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.isshepelev.auto.infrastructure.persistance.entity.MenuRevision;
import ru.isshepelev.auto.infrastructure.persistance.entity.enums.StatusRevision;

import java.util.Optional;

@Repository
public interface MenuRevisionRepository extends JpaRepository<MenuRevision, Long> {
    MenuRevision findTopByOrderByDateOfCreateDesc();

}
