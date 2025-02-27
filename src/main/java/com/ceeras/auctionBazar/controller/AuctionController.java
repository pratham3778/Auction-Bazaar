package com.ceeras.auctionBazar.controller;

import com.ceeras.auctionBazar.entity.Auction;
import com.ceeras.auctionBazar.service.AuctionService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @PostMapping
    public Auction createAuction(@RequestParam Long userId,
                                 @RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam LocalDateTime startDate,
                                 @RequestParam LocalDateTime endTime,
                                 @RequestParam BigDecimal startingPrice){
        //TODO: Implement
        return null;

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


    @DeleteMapping("/{auctionId}")
    public void deleteAuction(@PathVariable Long auctionId) {
        auctionService.deleteAuction(auctionId);
    }


}
