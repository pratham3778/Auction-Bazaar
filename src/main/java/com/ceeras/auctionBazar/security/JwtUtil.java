package com.ceeras.auctionBazar.security;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    public String generateToken(String username) {

        return "dummy_token";
    }

    public boolean validateToken(String token, String username) {

        return true;
    }
}
