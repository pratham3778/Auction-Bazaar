package com.ceeras.auctionBazar.controller;

import com.ceeras.auctionBazar.entity.Auction;
import com.ceeras.auctionBazar.entity.User;
import com.ceeras.auctionBazar.service.AuctionService;
import com.ceeras.auctionBazar.repository.UserRepository;
import com.ceeras.auctionBazar.Security.JwtUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private AuctionService auctionService;
    
    public AuctionController(AuctionService auctionService, UserRepository userRepository, JwtUtil jwtUtil) {
            this.auctionService = auctionService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/create-auction")
    public ResponseEntity<Auction> createAuction(@RequestBody Map<String, Object> requestBody) {
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        String title = requestBody.get("title").toString();
        String description = requestBody.get("description").toString();
        LocalDateTime startDate = LocalDateTime.parse(requestBody.get("startDate").toString());
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime").toString());
        BigDecimal startingPrice = new BigDecimal(requestBody.get("startingPrice").toString());

        Auction auction = auctionService.createAuction(userId, title, description, startDate, endTime, startingPrice);
        return ResponseEntity.ok(auction);
    }


    @GetMapping("getAuction/{auctionId}")
    public Auction getAuction(@PathVariable Long auctionId) {
        return auctionService.getAuction(auctionId);
    }

    @GetMapping("/getAll-Auctions")
    public List<Auction> getAllAuctions() {
        return auctionService.getAllAuctions();
    }

    @PutMapping("/{auctionId}")
    public ResponseEntity<?> updateAuction(@PathVariable Long auctionId, 
                                        @RequestHeader("Authorization") String token,
                                        @RequestBody Map<String, Object> requestBody) {
        
        String jwt = token.substring(7);
        String userEmail = jwtUtil.getEmailFromToken(jwt);
        Optional<User> user = userRepository.findByEmail(userEmail);
        
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User not found");
        }

        Long userId = user.get().getId();
        String title = requestBody.get("title").toString();
        String description = requestBody.get("description").toString();
        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime").toString());
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime").toString());
        BigDecimal startingPrice = new BigDecimal(requestBody.get("startingPrice").toString());

        try {
            Auction updatedAuction = auctionService.updateAuction(auctionId, userId, title, description, startTime, endTime, startingPrice);
            return ResponseEntity.ok(updatedAuction);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }





    @DeleteMapping("/deleteAuction/{auctionId}")
    public void deleteAuction(@PathVariable Long auctionId) {
        auctionService.deleteAuction(auctionId);
    }

}
