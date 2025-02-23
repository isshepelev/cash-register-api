package ru.isshepelev.auto.security.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.isshepelev.auto.security.entity.License;

@Repository
public interface LicenseRepository extends JpaRepository<License, Long> {

}
