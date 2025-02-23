package ru.isshepelev.auto.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.security.entity.User;

@Service
public class LicenseSecurityService {
    public boolean hasValidLicense(Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()){
            return false;
        }
        User user = (User) authentication.getPrincipal();
        return user.hasValidLicense();
    }

}
