package ru.isshepelev.auto.infrastructure.service;

import org.springframework.stereotype.Service;
import ru.isshepelev.auto.infrastructure.persistance.entity.JobTitle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface JobTitleService {

    void addJobTitle(String newRole);

    List<JobTitle> getJobTitle();

    Optional<JobTitle> getJobTitleById(UUID id);

    void deleteJobTitleById(UUID id);
}
