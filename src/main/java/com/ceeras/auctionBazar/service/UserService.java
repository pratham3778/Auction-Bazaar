package com.ceeras.auctionBazar.service;

import com.ceeras.auctionBazar.Security.JwtUtil;
import com.ceeras.auctionBazar.email_notification.sendmail;
import com.ceeras.auctionBazar.entity.EmailDetails;
import com.ceeras.auctionBazar.entity.User;
import com.ceeras.auctionBazar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService {

    
    private final UserRepository userRepository;
    private final sendmail send;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    // Password must have at least 6 characters, 1 uppercase, 1 lowercase, 1 digit, and 1 special character
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$");

    @Autowired
    public UserService(UserRepository userRepository, JwtUtil jwtUtil, sendmail send) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.send=send;
    }

    public String registerUser(String email, String password, String name) {
        if (!EMAIL_PATTERN.matcher(email).matches()) return "Invalid email address";

        if (userRepository.findByEmail(email).isPresent()) return "Email address already in use";

        if (!PASSWORD_PATTERN.matcher(password).matches())
            return "Password must be at least 6 characters and contain at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character";

       User savedUser = userRepository.save(new User(null, email, bCryptPasswordEncoder.encode(password),null,null, name, User.Role.USER, LocalDateTime.now(), null, null));
       send.sendEmail(email, name,"1");
        return "User registered successfully! ID: "+savedUser.getId();
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
      public String requestPasswordReset(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return "User not found";
        }

        User user = userOptional.get();
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        long currentTime = System.currentTimeMillis();
        user.setResetTokenCreationTime(currentTime);
        userRepository.save(user);
        String frontendResetLink = "http://your-frontend-url.com/reset-password?token=" + token;//frontend link to be modified with original
        send.sendEmail(email, user.getName(), "3", frontendResetLink);
        return "Password reset link sent to your email";
    }

    public String resetPassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isEmpty()) {
            return "Invalid token";
        }
        User user = userOptional.get();
        long currentTime = System.currentTimeMillis();
        long tokenCreationTime = user.getResetTokenCreationTime();
        long expirationTime = 1 * 60 * 1000; // 15 minutes
        if (currentTime - tokenCreationTime > expirationTime) {
            return "Token has expired";
        }
        user.setPassword(newPassword);
        user.setResetToken(null);
        userRepository.save(user);

        return "Password reset successfully";
    }
}