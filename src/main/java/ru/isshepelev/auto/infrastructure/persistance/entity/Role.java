package ru.isshepelev.auto.infrastructure.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Role {
    @Id
    private UUID id;
    private String role;
    @OneToMany(mappedBy = "role")
    @ToString.Exclude
    private List<Employee> employees;

}
