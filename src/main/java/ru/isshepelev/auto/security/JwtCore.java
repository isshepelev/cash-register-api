package ru.isshepelev.auto.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import java.util.Date;

@Component
public class JwtCore {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expirationMS}")
    private int lifeTime;

    public String generateToken(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuer(String.valueOf(new Date()))
                .setExpiration(new Date((new Date()).getTime() + lifeTime))
                .claim("role", userDetails.getRole())
                .compact();
    }

    public String getNameFromJwt(String token){
        return Jwts.parser().setSigningKey(secret).build().parseSignedClaims(token).getPayload().getSubject();
    }

//    public String getRoleFromJwt(String token){
//        return Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).getBody().ge; TODO сделать получение роли
//    }
}
