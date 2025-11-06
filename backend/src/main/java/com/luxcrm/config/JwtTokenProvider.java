package com.luxcrm.config;

import java.util.Date;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import com.luxcrm.enums.Role;

@Component
public class JwtTokenProvider {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    private final long validityInMs = 1000 * 60 * 60; // 1 hour

    public String generateToken(String email, Role role){

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
