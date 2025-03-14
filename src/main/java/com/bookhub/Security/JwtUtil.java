package com.bookhub.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    public String generateToken(String userEmail, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return Jwts.builder()
                .claims(claims)
                .subject(userEmail)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SECRET_KEY)
                .compact();
    }
    public String extractUserEmail(String token) {
        return extractAllClaims(token).getSubject();
    }
    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }
    public boolean validateToken(String token, String userEmail) {
        final String extractedUserEmail = extractUserEmail(token);
        return (extractedUserEmail.equals(userEmail) && !isTokenExpired(token));
    }
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
    //Methods to extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

