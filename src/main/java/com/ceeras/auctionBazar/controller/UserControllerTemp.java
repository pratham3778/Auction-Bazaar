package com.ceeras.auctionBazar.controller;

import com.ceeras.auctionBazar.entity.User;
import com.ceeras.auctionBazar.service.UserServiceTemp;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserControllerTemp {

    private final UserServiceTemp userService;

    public UserControllerTemp(UserServiceTemp userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody Map<String, String> userData) {
        String email = userData.get("email");
        String password = userData.get("password");
        String name = userData.get("name");

        return userService.registerUser(email, password, name);
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
}
