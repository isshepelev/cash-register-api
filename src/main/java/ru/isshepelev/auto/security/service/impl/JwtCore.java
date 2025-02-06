package ru.isshepelev.auto.security.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import ru.isshepelev.auto.security.entity.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtCore {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expirationMS}")
    private int lifeTime;

    public String generateToken(User userDetails){
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + lifeTime);
        return Jwts.builder()
                .claim("role" , userDetails.getRole())
                .claim("userId", userDetails.getId())
                .claim("licenseOfExpiration", userDetails.getLicense().getDateOfExpiration())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + lifeTime))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsResolvers){
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token).getBody();
    }

    public String getRoleFromJwt(String token){
        return (String) Jwts.parser().setSigningKey(secret).build().parseSignedClaims(token).getPayload().get("role");
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    private boolean isTokenExpired(String token) {
        return extractClaims(token,Claims::getExpiration).before(new Date());
    }
}
