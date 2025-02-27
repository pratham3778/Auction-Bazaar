package com.ceeras.auctionBazar.controller;

import com.ceeras.auctionBazar.entity.Auction;
import com.ceeras.auctionBazar.service.AuctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
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
    public Auction updateAuction(@PathVariable Long auctionId,
                                 @RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam LocalDateTime startTime,
                                 @RequestParam LocalDateTime endTime,
                                 @RequestParam BigDecimal startingPrice) {
        return null; // TODO: Implement
    }




    @DeleteMapping("/deleteAuction/{auctionId}")
    public void deleteAuction(@PathVariable Long auctionId) {
        auctionService.deleteAuction(auctionId);
    }

}
