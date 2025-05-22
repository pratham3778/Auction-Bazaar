package com.ceeras.auctionBazar.controller;

import com.ceeras.auctionBazar.dto.BidDTO;
import com.ceeras.auctionBazar.dto.BidRequestDTO;
import com.ceeras.auctionBazar.entity.Auction;
import com.ceeras.auctionBazar.entity.Bid;
import com.ceeras.auctionBazar.entity.User;
import com.ceeras.auctionBazar.service.AuctionService;
import com.ceeras.auctionBazar.service.BidService;
import com.ceeras.auctionBazar.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bids")
@CrossOrigin(origins = "*")
public class BidController {

    private final BidService bidService;
    private final AuctionService auctionService;
    private final UserService userService;

    public BidController(BidService bidService, AuctionService auctionService, UserService userService) {
        this.bidService = bidService;
        this.auctionService = auctionService;
        this.userService = userService;
    }

    @PostMapping("/place")
    public ResponseEntity<BidDTO> placeBid(@RequestHeader("Authorization") String jwt,
                                           @RequestBody BidRequestDTO bidRequest) {
        String email = userService.extractEmailFromToken(jwt.replace("Bearer ", ""));
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Auction auction = auctionService.getAuctionById(bidRequest.getAuctionId());

        Bid bid = bidService.placeBid(user, auction, bidRequest.getAmount());

        BidDTO dto = bidService.mapToDTO(bid);
        return ResponseEntity.ok(dto);
    }

    //Get All Bids for a Specific Auction
    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<List<BidDTO>> getBidsByAuction(@PathVariable Long auctionId) {
        List<BidDTO> bids = bidService.getBidsByAuction(auctionId);
        return ResponseEntity.ok(bids);
    }

    //if a user wants to see their own bid history.
    @GetMapping("/my-bids")
    public ResponseEntity<List<BidDTO>> getBidsByUser(@RequestHeader("Authorization") String jwt) {
        String email = userService.extractEmailFromToken(jwt.replace("Bearer ", ""));
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<BidDTO> bids = bidService.getBidsByUser(user);
        return ResponseEntity.ok(bids);
    }

    //Get All Bids 
    @GetMapping("/all")
    public ResponseEntity<List<BidDTO>> getAllBids() {
        List<BidDTO> bids = bidService.getAllBids();
        return ResponseEntity.ok(bids);
    }



}