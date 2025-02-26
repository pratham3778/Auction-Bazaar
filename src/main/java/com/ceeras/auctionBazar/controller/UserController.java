package com.ceeras.auctionBazar.controller;

import com.ceeras.auctionBazar.dto.UserRegisterDTO;
import com.ceeras.auctionBazar.dto.UserLoginDTO;
import com.ceeras.auctionBazar.entity.User;
import com.ceeras.auctionBazar.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth") // Matching friend's approach
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    public UserController(UserService userService) {
//        this.userService = userService;
//    }

    // Registration API
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody UserRegisterDTO userDTO) {
        String response = userService.registerUser(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword());
        return ResponseEntity.ok(Map.of("message", response));
    }

    // Login API
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserLoginDTO loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        String token = userService.loginUser(email, password);
        if ("Invalid email or password!".equals(token)) {
            return ResponseEntity.badRequest().body(Map.of("error", token));
        }
        return ResponseEntity.ok(Map.of("token", token));
    }

    // Update API
    @PutMapping("/update")
    public ResponseEntity<Map<String, String>> updateUser(@RequestHeader("Authorization") String jwt,
                                                          @Valid @RequestBody UserRegisterDTO userDTO) {
        Optional<User> mainUser = userService.getUserByEmail(userDTO.getEmail());
        if (mainUser.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }

        User updatedUser = new User();
        updatedUser.setEmail(userDTO.getEmail());
        updatedUser.setPassword(userDTO.getPassword());
        updatedUser.setName(userDTO.getName());

        userService.update(mainUser.get(), updatedUser);
        return ResponseEntity.ok(Map.of("message", "User updated successfully"));
    }
}
