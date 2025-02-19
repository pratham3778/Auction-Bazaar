package com.ceeras.auctionBazar.service;

import com.ceeras.auctionBazar.entity.User;
import com.ceeras.auctionBazar.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    private UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public String registerUser(String email, String password, String name) {
        if(!EMAIL_PATTERN.matcher(email).matches()) {
            return "Invalid email address";
        }

        Optional<User> existingUser = userRepository.findByEmail(email);

        if(existingUser.isPresent()) {
            return "Email address already in use";
        }

        if(password.length() < 6) {
            return "Password must be at least 6 characters";
        }

        String encodedPassword = bCryptPasswordEncoder.encode(password);
        User newUser = new User(null, email, encodedPassword, name);
        User savedUser = userRepository.save(newUser);

        return "User registered successfully! ID: " + savedUser.getId();

    }
}
