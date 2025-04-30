package ru.isshepelev.auto.infrastructure.persistance.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value =
            "SELECT e FROM Employee e " +
                    "JOIN e.jobTitle j " +
                    "WHERE j.role = :role " +
                    "ORDER BY e.surname")
    List<Employee> findEmployeesByJobTitleRole(@Param("role") String role);

    @Query(value =
            "SELECT * FROM Employee e " +
                    "JOIN JobTitle j ON e.jobTitle_id = j.id " +
                    "WHERE j.role = :role " +
                    "ORDER BY e.surname", nativeQuery = true)
    List<Employee> findEmployeesByJobTitleRoleNative(@Param("role") String role);

    @Query(value =
            "SELECT j.role, COUNT(e.id) FROM Employee e " +
                    "JOIN e.jobTitle j " +
                    "GROUP BY j.role", nativeQuery = true)
    List<Object[]> countEmployeesByJobTitle();
}
