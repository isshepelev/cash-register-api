package ru.isshepelev.auto.infrastructure.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.isshepelev.auto.infrastructure.persistance.entity.Employee;
import ru.isshepelev.auto.infrastructure.persistance.entity.JobTitle;
import ru.isshepelev.auto.security.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByPersonalCode(int personalCode);
    boolean existsByJobTitle(JobTitle jobTitle);
    List<Employee> findByOwner(User owner);
}
