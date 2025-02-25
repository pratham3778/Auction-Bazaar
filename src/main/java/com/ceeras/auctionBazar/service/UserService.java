package com.ceeras.auctionBazar.service;

import com.ceeras.auctionBazar.Security.JwtUtil;
import com.ceeras.auctionBazar.entity.User;
import com.ceeras.auctionBazar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    // Password must have at least 6 characters, 1 uppercase, 1 lowercase, 1 digit, and 1 special character
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$");

    @Autowired
    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String registerUser(String email, String password, String name) {
        if (!EMAIL_PATTERN.matcher(email).matches()) return "Invalid email address";

        if (userRepository.findByEmail(email).isPresent()) return "Email address already in use";

        if (!PASSWORD_PATTERN.matcher(password).matches())
            return "Password must be at least 6 characters and contain at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character";

        User savedUser = userRepository.save(new User(null, email, bCryptPasswordEncoder.encode(password), name, User.Role.USER, LocalDateTime.now(), null, null));
        return "User registered successfully! ID: ";
    }

    public User update(User user, User user2) {
        return userRepository.findByEmail(user.getEmail()).map(oldUser -> {
            if (user2.getEmail() != null && !user2.getEmail().equals(oldUser.getEmail()) && userRepository.findByEmail(user2.getEmail()).isPresent()) {
                throw new RuntimeException("Email address already in use");
            }

            if (user2.getEmail() != null) {
                if (!EMAIL_PATTERN.matcher(user2.getEmail()).matches())
                    throw new RuntimeException("Invalid email address");
                oldUser.setEmail(user2.getEmail());
            }
            if (user2.getPassword() != null) {
                if (!PASSWORD_PATTERN.matcher(user2.getPassword()).matches())
                    throw new RuntimeException("Password must be at least 6 characters and contain at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character");
                oldUser.setPassword(bCryptPasswordEncoder.encode(user2.getPassword()));
            }
            if (user2.getRole() != null) oldUser.setRole(user2.getRole());
            if (user2.getName() != null) oldUser.setName(user2.getName());

            return userRepository.save(oldUser);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String login(String email, String password) {
        return userRepository.findByEmail(email).map(user ->
                bCryptPasswordEncoder.matches(password, user.getPassword())
                        ? "Login successful. Token: " + jwtUtil.generateToken(email)
                        : "Incorrect password"
        ).orElse("User not found");
    }

    public String deleteUser(String username, String jwt) {
        var userOptional = userRepository.findByEmail(username);
        if (userOptional.isEmpty()) return "User not found";

        User user=userOptional.get();
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), new ArrayList<>()
        );

        if (!jwtUtil.validateToken(jwt, userDetails)) return "Invalid token";
        String userName=user.getEmail();
        userRepository.delete(userOptional.get());
        return "User deleted successfully "+userName;
    }
}