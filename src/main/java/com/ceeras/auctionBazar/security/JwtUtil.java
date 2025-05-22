package com.ceeras.auctionBazar.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    // JWT secret key and expiration (24 hours)
    private final String jwtSecret = "ThisIsASecretKeyForJwtWhichShouldBeLongEnough123456";
    private final long jwtExpirationMs = 86400000; // 1 day


    // Signing key generated from the secret
    private final Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

    // Generate JWT token with username
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Set username as subject
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // Token expiry
                .signWith(key) // Sign the token
                .compact();
    }

    // Validate that the token is not expired and matches the given username
    public boolean validateToken(String token, String username) {
        try {
            String tokenUsername = extractUsername(token);
            return (tokenUsername.equals(username) && !isTokenExpired(token));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Extract username from token
    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // Check if token has expired
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()) // your key method
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        return null;
    }


}
