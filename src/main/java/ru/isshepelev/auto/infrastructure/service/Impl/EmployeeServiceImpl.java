package ru.isshepelev.auto.infrastructure.service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.infrastructure.persistance.entity.Employee;
import ru.isshepelev.auto.infrastructure.persistance.repository.EmployeeRepository;
import ru.isshepelev.auto.infrastructure.service.EmployeeService;
import ru.isshepelev.auto.infrastructure.service.dto.EmployeeCreateDto;
import ru.isshepelev.auto.infrastructure.service.dto.EmployeeEditDto;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployee(){
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(UUID id){
        return Optional.of(employeeRepository.findById(id).get());
    }

    @Override
    public void createEmployee(EmployeeCreateDto employeeDto){
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setName(employeeDto.getName());
        employee.setSurname(employeeDto.getSurname());
        employee.setPersonalCode(100 + new Random().nextInt(900));

        while (findEmployeeByPersonalCode(employee.getPersonalCode()).isPresent()){
            employee.setPersonalCode(100 + new Random().nextInt(900));
        }

        employee.setRole(employeeDto.getRole());
        employee.setCashRegisterAccessible(employeeDto.isCashRegisterAccessible());
        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployeeById(UUID id){
        employeeRepository.deleteById(id);
    }

    @Override
    public Optional<Employee> findEmployeeByPersonalCode(int personalCode) {
        return employeeRepository.findByPersonalCode(personalCode);
    }

    @Override
    @Transactional
    public void update(UUID id, EmployeeEditDto dto){
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()){
            Employee employee = employeeOptional.get();
            employee.setRole(dto.getRole());
            employee.setName(dto.getName());
            employee.setSurname(dto.getSurname());
            employee.setCashRegisterAccessible(dto.isCashRegisterAccessible());
            log.info("изменение сотрудника на {}", dto);
            employeeRepository.save(employee);
        }
    }
}
