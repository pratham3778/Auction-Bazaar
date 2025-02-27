package com.ceeras.auctionBazar.service;

import com.ceeras.auctionBazar.entity.Auction;
import com.ceeras.auctionBazar.entity.User;
import com.ceeras.auctionBazar.repository.AuctionRepository;
import com.ceeras.auctionBazar.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    public AuctionService(AuctionRepository auctionRepository, UserRepository userRepository) {
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
    }

    public Auction createAuction(Long userId, String title, String description, LocalDateTime startTime, LocalDateTime endTime, BigDecimal startingPrice) {
        Optional<User> creator = userRepository.findById(userId);
        if (creator.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Auction auction = new Auction();
        auction.setCreator(creator.get());
        auction.setTitle(title);
        auction.setDescription(description);
        auction.setStartTime(startTime);
        auction.setEndTime(endTime);
        auction.setStartingPrice(startingPrice);
        auction.setCreatedAt(LocalDateTime.now());
        auction.setUpdatedAt(LocalDateTime.now());

        return auctionRepository.save(auction);
    }

    public Auction updateAuction(Long auctionId, String title, String description, LocalDateTime startTime, LocalDateTime endTime, BigDecimal startingPrice) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        auction.setTitle(title);
        auction.setDescription(description);
        auction.setStartTime(startTime);
        auction.setEndTime(endTime);
        auction.setStartingPrice(startingPrice);


        return auctionRepository.save(auction);
    }

    public void deleteAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        auctionRepository.delete(auction);
    }
}
