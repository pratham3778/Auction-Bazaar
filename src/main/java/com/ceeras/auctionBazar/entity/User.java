package com.ceeras.auctionBazar.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    @Column(nullable = true)
    private String resetToken;
    private Long resetTokenCreationTime; 

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonBackReference
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Auction> auctions;

    @JsonBackReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Bid> bids;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    public String getResetToken() {
        return resetToken;
    }
    public Long getResetTokenCreationTime() {
        return resetTokenCreationTime;
    }

    public void setResetTokenCreationTime(Long resetTokenCreationTime) {
        this.resetTokenCreationTime = resetTokenCreationTime;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
    public enum Role {
        USER,
        ADMIN
    }
}
