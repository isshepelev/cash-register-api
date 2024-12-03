package ru.isshepelev.auto.infrastructure.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.infrastructure.persistance.entity.Employee;
import ru.isshepelev.auto.infrastructure.service.dto.EmployeeCreateDto;
import ru.isshepelev.auto.infrastructure.service.dto.EmployeeEditDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface EmployeeService {

    List<Employee> getAllEmployee();

    Optional<Employee> getEmployeeById(UUID id);

    void createEmployee(EmployeeCreateDto employeeDto);

    void deleteEmployeeById(UUID id);

    Optional<Employee> findEmployeeByPersonalCode(int personalCode);

    @Transactional
    void update(UUID id, EmployeeEditDto dto);
}
