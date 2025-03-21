package com.ceeras.auctionBazar.controller;

import com.ceeras.auctionBazar.Security.JwtUtil;
import com.ceeras.auctionBazar.entity.User;
import com.ceeras.auctionBazar.repository.UserRepository;
import com.ceeras.auctionBazar.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private UserRepository userRepository;
    private  JwtUtil jwtService;

    public UserController(UserService userService, JwtUtil jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> userData) {
        String email = userData.get("email");
        String password = userData.get("password");
        String name = userData.get("name");

        String result = userService.registerUser(email, password, name);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> userData) {
        String email = userData.get("email");
        String password = userData.get("password");

        String result = userService.login(email, password);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        String username = jwtService.getEmailFromToken(jwt);
        String result = userService.deleteUser(username,jwt);
        return ResponseEntity.ok(result);
    }
    @PutMapping("/update")
    public User update(@RequestHeader("Authorization") String jwt, @RequestBody Map<String, String> userData) {
        String email = userData.get("email");

        Optional<User> mainUser = userService.getUserByEmail(email);
        if (mainUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User updatedUser = new User();
        updatedUser.setEmail(userData.get("email"));
        updatedUser.setPassword(userData.get("password"));
        updatedUser.setName(userData.get("name"));

        return userService.update(mainUser.get(), updatedUser);
    }
    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestBody Map<String, String> userData) {
        String email = userData.get("email");
        String result = userService.requestPasswordReset(email);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> userData) {
        String token = userData.get("token");
        String newPassword = userData.get("newPassword");
        String result = userService.resetPassword(token, newPassword);
        return ResponseEntity.ok(result);
    }
}
