package ru.isshepelev.auto.security.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sub_users")
@Data
public class SubUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonBackReference
    private User owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "sub_user_roles",
            joinColumns = @JoinColumn(name = "sub_user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
