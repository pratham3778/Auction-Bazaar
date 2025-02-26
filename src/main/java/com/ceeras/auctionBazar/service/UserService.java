package com.ceeras.auctionBazar.service;

import com.ceeras.auctionBazar.entity.User;
import com.ceeras.auctionBazar.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.ceeras.auctionBazar.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String registerUser(String name, String email, String password) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return "Email is already in use!";
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
        return "User registered successfully!";
    }

    public String loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty() || !passwordEncoder.matches(password, userOptional.get().getPassword())) {
            return "Invalid email or password!";
        }
        return  email; //jwtUtil.generateToken(email);
        //return jwtUtil.generateToken(email);
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
                oldUser.setPassword(passwordEncoder.encode(newPassword)); // Fixed variable name
            }

            if (user2.getRole() != null && user2.getRole() != oldUser.getRole()) {
                oldUser.setRole(user2.getRole());
            }

            if (user2.getName() != null) {
                oldUser.setName(user2.getName());
            }
            if(user2.getAuctions()!=null){
                oldUser.setAuctions(user2.getAuctions());
            }
            if(user2.getBids()!=null){
                oldUser.setBids(user2.getBids());
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
