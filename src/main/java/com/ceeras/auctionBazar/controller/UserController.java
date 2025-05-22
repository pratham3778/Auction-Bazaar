package com.ceeras.auctionBazar.controller;

import com.ceeras.auctionBazar.dto.AuthResponseDTO;
import com.ceeras.auctionBazar.dto.UserRegisterDTO;
import com.ceeras.auctionBazar.dto.UserLoginDTO;
import com.ceeras.auctionBazar.entity.User;
import com.ceeras.auctionBazar.service.UserService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<AuthResponseDTO> login(@RequestBody UserLoginDTO loginDTO) {
        String token = userService.loginUser(loginDTO.getEmail(), loginDTO.getPassword());
        if (token.equals("Invalid email or password!")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponseDTO(null, "Invalid credentials"));
        }
        return ResponseEntity.ok(new AuthResponseDTO(token, "Login successful"));
    }


    // Update API
    @PutMapping("/update")
    public ResponseEntity<Map<String, String>> updateUser(@RequestHeader("Authorization") String jwt,
                                                          @Valid @RequestBody UserRegisterDTO userDTO) {
        String token = jwt.replace("Bearer ", "");
        String email = userService.extractEmailFromToken(token); // You’ll create this method

        Optional<User> mainUser = userService.getUserByEmail(email);
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

   //delete API
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteUser(@RequestHeader("Authorization") String jwt) {
        String token = jwt.replace("Bearer ", "");
        String email = userService.extractEmailFromToken(token);

        boolean deleted = userService.deleteByEmail(email);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }




}
