package ru.isshepelev.auto.infrastructure.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.infrastructure.persistance.entity.JobTitle;
import ru.isshepelev.auto.infrastructure.persistance.repository.EmployeeRepository;
import ru.isshepelev.auto.infrastructure.persistance.repository.JobTitleRepository;
import ru.isshepelev.auto.infrastructure.service.JobTitleService;
import ru.isshepelev.auto.security.entity.User;
import ru.isshepelev.auto.security.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobTitleServiceImpl implements JobTitleService {

    private final JobTitleRepository jobTitleRepository;
    private final EmployeeRepository employeeRepository;
    private final UserService userService;

    @Override
    public void addJobTitle(String newJobTitle) {
        for (JobTitle roles : getJobTitle()){
            if (roles.getRole().toLowerCase().equals(newJobTitle.toLowerCase())){
                log.warn("Должность уже существует. Добавлена не будет");
                return;
            }
        }
        JobTitle jobTitle = new JobTitle();
        jobTitle.setId(UUID.randomUUID());
        jobTitle.setRole(newJobTitle);
        User user = userService.getUserByUsername(userService.getUsernameAuthorizedUser());
        if (user == null){
            throw new UsernameNotFoundException("User not found");
        }
        jobTitle.setOwner(user);
        log.info("Добавление новой должности {}", jobTitle);
        jobTitleRepository.save(jobTitle);
    }

    @Override
    public List<JobTitle> getJobTitle() {
        return jobTitleRepository.findAll().stream()
                .filter(role -> role.getOwner().getUsername().equals(userService.getUsernameAuthorizedUser()))
                .toList();

    }

    @Override
    public Optional<JobTitle> getJobTitleById(UUID id) {
        return jobTitleRepository.findById(id)
                .filter(role -> role.getOwner().getUsername().equals(userService.getUsernameAuthorizedUser()));
    }

    @Override
    public void deleteJobTitleById(UUID id) {
        JobTitle role = getJobTitleById(id).orElseThrow(() -> new RuntimeException("должность не найдена"));
        if (employeeRepository.existsByJobTitle(role)){
            throw new RuntimeException("Невозможно удалить должность, так как она используется сотрудниками");
        }
        log.info("Удаление роли с id {}", id);
        jobTitleRepository.deleteById(id);
    }

}
