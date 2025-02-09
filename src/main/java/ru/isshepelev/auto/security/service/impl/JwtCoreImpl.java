package ru.isshepelev.auto.security.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import ru.isshepelev.auto.security.entity.User;
import ru.isshepelev.auto.security.service.JwtCore;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtCoreImpl implements JwtCore{
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expirationMS}")
    private int lifeTime;

    @Override
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

    @Override
    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + lifeTime + 10000))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    @Override
    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolvers){
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token).getBody();
    }

    @Override
    public String getRoleFromJwt(String token){
        return (String) Jwts.parser().setSigningKey(secret).build().parseSignedClaims(token).getPayload().get("role");
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractClaims(token,Claims::getExpiration).before(new Date());
    }
}
