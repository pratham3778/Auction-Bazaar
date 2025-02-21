package com.ceeras.auctionBazar.service;

import com.ceeras.auctionBazar.entity.User;
import com.ceeras.auctionBazar.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserServiceTemp {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public UserServiceTemp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerUser(String email, String password, String name) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "Invalid email address";
        }

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return "Email address already in use";
        }

        if (password.length() < 6) {
            return "Password must be at least 6 characters";
        }

        String encodedPassword = bCryptPasswordEncoder.encode(password);
        User newUser = new User(null, User.Role.USER, email, encodedPassword, name);
        User savedUser = userRepository.save(newUser);

        return "User registered successfully! ID: " + savedUser.getId();
    }

    public User update(User user, User user2) {
        Optional<User> old = userRepository.findByEmail(user.getEmail());
        if (old.isPresent()) {
            User oldUser = old.get();

            String email = user2.getEmail();
            if (email != null && !email.equals(oldUser.getEmail())) {
                if (userRepository.findByEmail(email).isPresent()) {
                    throw new RuntimeException("Email address already in use");
                }
                oldUser.setEmail(email);
            }

            String newPassword = user2.getPassword();
            if (newPassword != null && newPassword.length() < 6) {
                throw new RuntimeException("Password must be at least 6 characters");
            } else if (newPassword != null) {
                oldUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
            }

            if (user2.getRole() != null && user2.getRole() != oldUser.getRole()) {
                oldUser.setRole(user2.getRole());
            }

            if (user2.getName() != null) {
                oldUser.setName(user2.getName());
            }

            return userRepository.save(oldUser);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
