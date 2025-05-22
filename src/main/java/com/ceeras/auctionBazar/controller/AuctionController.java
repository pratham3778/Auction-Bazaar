package com.ceeras.auctionBazar.controller;

import com.ceeras.auctionBazar.dto.AuctionCreateDTO;
import com.ceeras.auctionBazar.dto.AuctionResponseDTO;
import com.ceeras.auctionBazar.dto.AuctionUpdateDTO;
import com.ceeras.auctionBazar.entity.Auction;
import com.ceeras.auctionBazar.entity.User;
import com.ceeras.auctionBazar.service.AuctionService;
import com.ceeras.auctionBazar.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") //add @CrossOrigin at the top of controller for testing with a frontend later

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    private final AuctionService auctionService;
    private final UserService userService;

    public AuctionController(AuctionService auctionService, UserService userService) {
        this.auctionService = auctionService;
        this.userService = userService;
    }

    //Create Auction
    //  CREATE AUCTION (returns AuctionResponseDTO)
    @PostMapping("/create")
    public ResponseEntity<AuctionResponseDTO> createAuction(@RequestHeader("Authorization") String jwt,
                                                            @RequestBody @Valid AuctionCreateDTO dto) {
        String email = userService.extractEmailFromToken(jwt.replace("Bearer ", ""));
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Auction auction = auctionService.createAuction(
                user.getId(),
                dto.getTitle(),
                dto.getDescription(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getStartingPrice()
        );

        AuctionResponseDTO responseDTO = auctionService.mapToDto(auction);
        return ResponseEntity.ok(responseDTO);
    }

    // Read All Auctions
    @GetMapping
    public ResponseEntity<List<AuctionResponseDTO>> getAllAuctions() {
        return ResponseEntity.ok(auctionService.getAllAuctionDTOs());
    }

    // Read Auction by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAuctionById(@PathVariable Long id) {
        try {
            AuctionResponseDTO dto = auctionService.getAuctionDTOById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body("Auction with ID " + id + " not found");
        }
    }

    // Get All Auctions by Authenticated User
        @GetMapping("/my-auctions")
    public ResponseEntity<List<AuctionResponseDTO>> getMyAuctions(@RequestHeader("Authorization") String jwt) {
        String email = userService.extractEmailFromToken(jwt.replace("Bearer ", ""));
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<AuctionResponseDTO> userAuctions = auctionService.getAuctionsByUserId(user.getId());
        return ResponseEntity.ok(userAuctions);
    }


    // Update Auction
    @PutMapping("/update/{id}")
    public ResponseEntity<AuctionResponseDTO> updateAuction(@PathVariable Long id,
                                                            @RequestBody @Valid AuctionUpdateDTO dto) {
        Auction updated = auctionService.updateAuction(
                id,
                dto.getTitle(),
                dto.getDescription(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getStartingPrice()
        );
        AuctionResponseDTO responseDTO = auctionService.mapToDto(updated);
        return ResponseEntity.ok(responseDTO);
    }

    // Delete Auction
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAuction(@PathVariable Long id) {
        try {
            auctionService.deleteAuction(id);
            return ResponseEntity.ok("Auction deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Auction with ID " + id + " not found");
        }
    }

}
