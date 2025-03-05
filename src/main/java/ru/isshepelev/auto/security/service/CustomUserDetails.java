package ru.isshepelev.auto.security.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.isshepelev.auto.security.entity.SubUser;
import ru.isshepelev.auto.security.entity.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
@Data
public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    private Set<GrantedAuthority> authorities;
    private Long ownerId;

    public CustomUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        this.ownerId = user.getId();
    }

    public CustomUserDetails(SubUser subUser) {
        this.username = subUser.getUsername();
        this.password = subUser.getPassword();
        this.authorities = subUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        this.ownerId = subUser.getOwner().getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}