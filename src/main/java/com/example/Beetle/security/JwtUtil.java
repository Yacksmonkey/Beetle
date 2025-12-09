package com.example.Beetle.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String SECRET_KEY =
            "beetle_secret_key_1234567890_beetle_secret_key_1234567890";

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private final long expirationMs = 86400000; // 24 hours

    // Generate token with userId, email, roles
    public String generateToken(Long userId, String email, List<String> roles) {

        return Jwts.builder()
                .setSubject(email)
                .addClaims(Map.of(
                        "userId", userId.toString(),
                        "roles", roles
                ))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    // Validate JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return (List<String>) claims.get("roles");
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return Long.valueOf((String) claims.get("userId"));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
