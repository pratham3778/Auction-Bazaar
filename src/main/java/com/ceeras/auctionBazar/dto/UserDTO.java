package com.ceeras.auctionBazar.dto;

import com.ceeras.auctionBazar.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String role;

    public UserDTO(Long id, String email, String name, User.Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role.name();
    }
}
