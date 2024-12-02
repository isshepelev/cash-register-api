package ru.isshepelev.auto.infrastructure.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.isshepelev.auto.infrastructure.persistance.entity.Menu;

import java.util.UUID;

@Repository
public interface MenuRepositroty extends JpaRepository<Menu, UUID> {

}
