package ru.isshepelev.auto.infrastructure.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isshepelev.auto.infrastructure.persistance.entity.Employee;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

}
