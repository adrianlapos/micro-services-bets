package com.example.security;

import com.example.commons.Role;
import com.example.commons.UserResponseDTO;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private static final int EXPIRATION = 86400000;
    private final SecretKey key;
    private final JwtParser jwtParser;

    public JwtUtil(@Value("${jwt.secret}") String secret){
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.jwtParser =Jwts.parser()
                .verifyWith(key)
                .build();
    }

    public String generateToken(UserResponseDTO userResponseDTO){
        return Jwts.builder().subject(userResponseDTO.username())
                .claim("id",userResponseDTO.id())
                .claim("role",userResponseDTO.role().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION) ).signWith(key).compact();
    }

    public Claims extractClaims(String token){
        return this.jwtParser.parseSignedClaims(token).getPayload();
    }

    public String extractUsername(String token){
        Claims claims = this.extractClaims(token);
        return claims.getSubject();
    }

    public Long extractUserid(String token){
        Claims claims = this.extractClaims(token);
        return claims.get("id",Long.class);
    }

    public Role extractRole(String token){
        Claims claims = this.extractClaims(token);
        return Role.valueOf(claims.get("role",String.class));
    }

    public boolean validateToken(String token){

        try {
            this.jwtParser.parseSignedClaims(token);
            return true;
        }
        catch (JwtException | IllegalArgumentException e){
            return  false;
        }
    }
    public boolean isTokenValid(String jwt, UserResponseDTO userResponseDTO) {

        final String username = extractUsername(jwt);
        return (username.equals(userResponseDTO.username()) && !isTokenExpired(jwt));
    }

    public boolean isTokenExpired(String token){
        Date expiry = extractClaims(token).getExpiration();

        return expiry.before(new Date());
    }
}
