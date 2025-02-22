package ru.isshepelev.auto.infrastructure.service.Impl;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.infrastructure.persistance.entity.Employee;
import ru.isshepelev.auto.infrastructure.persistance.repository.EmployeeRepository;
import ru.isshepelev.auto.infrastructure.service.EmployeeService;
import ru.isshepelev.auto.infrastructure.service.dto.EmployeeCreateDto;
import ru.isshepelev.auto.infrastructure.service.dto.EmployeeEditDto;
import ru.isshepelev.auto.security.entity.User;
import ru.isshepelev.auto.security.repository.UserRepository;
import ru.isshepelev.auto.security.service.UserService;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserService userService;

    @Override
    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll().stream()
                .filter(employee -> employee.getJobTitle().getOwner().getUsername().equals(userService.getUsernameAuthorizedUser())).toList();
    }

    @Override
    public Optional<Employee> getEmployeeById(UUID id) {
        return employeeRepository.findById(id)
                .filter(employee -> employee.getJobTitle().getOwner().getUsername().equals(userService.getUsernameAuthorizedUser()));
    }

    @Override
    public void createEmployee(EmployeeCreateDto employeeDto) {
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setName(employeeDto.getName());
        employee.setSurname(employeeDto.getSurname());
        employee.setPersonalCode(generateUniquePersonalCode());
        employee.setJobTitle(employeeDto.getJobTitle());
        employee.setCashRegisterAccessible(employeeDto.isCashRegisterAccessible());

        User user = userService.getUserByUsername(userService.getUsernameAuthorizedUser());

        employee.setOwner(user);
        employeeRepository.save(employee);
    }

    private int generateUniquePersonalCode() {
        int personalCode;
        do {
            personalCode = 100 + new Random().nextInt(900);
        } while (findEmployeeByPersonalCode(personalCode).isPresent());
        return personalCode;
    }

    @Override
    public void deleteEmployeeById(UUID id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if (employeeOptional.isEmpty()) {
            throw new RuntimeException("Сотрудник не найден");
        }

        Employee employee = employeeOptional.get();
        if (!employee.getJobTitle().getOwner().getUsername().equals(userService.getUsernameAuthorizedUser())) {
            throw new AccessDeniedException("Нет прав на удаление данного сотрудника");
        }

        log.info("Удаление сотрудника с id {}", id);
        employeeRepository.deleteById(id);
    }

    @Override
    public Optional<Employee> findEmployeeByPersonalCode(int personalCode) {
        return employeeRepository.findByPersonalCode(personalCode)
                .filter(employee -> employee.getJobTitle().getOwner().getUsername().equals(userService.getUsernameAuthorizedUser()));
    }


    @Override
    @Transactional
    public void update(UUID id, EmployeeEditDto dto) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if (employeeOptional.isEmpty()) {
            throw new RuntimeException("Сотрудник не найден");
        }

        Employee employee = employeeOptional.get();

        if (!employee.getJobTitle().getOwner().getUsername().equals(userService.getUsernameAuthorizedUser())) {
            throw new AccessDeniedException("Нет прав на изменение данного сотрудника");
        }

        employee.setJobTitle(dto.getJobTitle());
        employee.setName(dto.getName());
        employee.setSurname(dto.getSurname());
        employee.setCashRegisterAccessible(dto.isCashRegisterAccessible());

        log.info("Изменение сотрудника с id {} на {}", id, dto);
        employeeRepository.save(employee);
    }

    @Override
    public Map<String, Object> checkAccess(int employeeCode, HttpSession session) {
        Optional<Employee> employeeOpt = findEmployeeByPersonalCode(employeeCode);

        if (employeeOpt.isEmpty()) {
            log.error("Работник с code: " + employeeCode + " не найден или нет доступа");
            throw new RuntimeException("Работник не найден или доступ запрещен");
        }

        Employee employee = employeeOpt.get();
        Map<String, Object> response = new HashMap<>();
        response.put("accessGranted", employee.isCashRegisterAccessible());

        if (employee.isCashRegisterAccessible()) {
            session.setAttribute("employeeName", employee.getName() + " " + employee.getSurname());
        }

        return response;
    }
}
