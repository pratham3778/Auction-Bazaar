package com.ceeras.auctionBazar.controller;

import com.ceeras.auctionBazar.dto.UserRegisterDTO;
import com.ceeras.auctionBazar.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //  Registration API
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody UserRegisterDTO userDTO) {
        String response = userService.registerUser(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword());
        return ResponseEntity.ok(Map.of("message", response));
    }

    //  Login API
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        String token = userService.loginUser(email, password);
        if (token.equals("Invalid email or password!")) {
            return ResponseEntity.badRequest().body(Map.of("error", token));
        }

        return ResponseEntity.ok(Map.of("token", token));
    }
    // Update api
    @PutMapping("/update-profile/{userId}")
    public ResponseEntity<Map<String, String>> updateUserProfile(
            @PathVariable Long userId,
            @RequestBody Map<String, String> updateRequest
    ) {
        String name = updateRequest.get("name");
        String email = updateRequest.get("email");
        String password = updateRequest.get("password");

        String response = userService.updateUserProfile(userId, name, email, password);
        return ResponseEntity.ok(Map.of("message", response));
    }
    // Delete api
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long userId) {
        String response = userService.deleteUser(userId);
        return ResponseEntity.ok(Map.of("message", response));
    }
}
