package ru.isshepelev.auto.security.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import ru.isshepelev.auto.security.entity.User;

import java.util.Map;
import java.util.function.Function;

public interface JwtCore {

    String generateToken(User userDetails);

    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);

    String extractUsername(String token);

    <T> T extractClaims(String token, Function<Claims, T> claimsResolvers);

    Claims extractAllClaims(String token);

    String getRoleFromJwt(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);
}
