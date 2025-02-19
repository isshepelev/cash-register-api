package ru.isshepelev.auto.infrastructure.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import ru.isshepelev.auto.security.entity.User;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Role implements  Serializable {
    @Id
    private UUID id;
    private String role;
    @OneToMany(mappedBy = "role")
    @ToString.Exclude
    private List<Employee> employees;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

}
