package com.ceeras.auctionBazar.service;

import com.ceeras.auctionBazar.entity.Auction;
import com.ceeras.auctionBazar.entity.User;
import com.ceeras.auctionBazar.repository.AuctionRepository;
import com.ceeras.auctionBazar.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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

    public Auction updateAuction(Long auctionId, Long userId, String title, String description, 
                             LocalDateTime startTime, LocalDateTime endTime, BigDecimal startingPrice) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        if (!auction.getCreator().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: You are not the owner of this auction");
        }

        auction.setTitle(title);
        auction.setDescription(description);
        auction.setStartTime(startTime);
        auction.setEndTime(endTime);
        auction.setStartingPrice(startingPrice);
        auction.setUpdatedAt(LocalDateTime.now());

        return auctionRepository.save(auction);
    }


    public void deleteAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        auctionRepository.delete(auction);
    }

    public Auction getAuction(Long auctionId) {
        return auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

    }

    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }
}
