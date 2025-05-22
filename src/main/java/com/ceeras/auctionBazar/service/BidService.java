package com.ceeras.auctionBazar.service;

import com.ceeras.auctionBazar.dto.BidDTO;
import com.ceeras.auctionBazar.entity.Auction;
import com.ceeras.auctionBazar.entity.Bid;
import com.ceeras.auctionBazar.entity.User;
import com.ceeras.auctionBazar.repository.AuctionRepository;
import com.ceeras.auctionBazar.repository.BidRepository;
import com.ceeras.auctionBazar.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BidService {

    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final EmailService emailService;

    public BidService(BidRepository bidRepository, UserRepository userRepository,
                      AuctionRepository auctionRepository, EmailService emailService) {
        this.bidRepository = bidRepository;
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.emailService = emailService;
    }

    // Place a bid on an auction
    public Bid placeBid(User user, Auction auction, BigDecimal amount) {
        // Save the new bid
        Bid bid = new Bid();
        bid.setUser(user);
        bid.setAuction(auction);
        bid.setAmount(amount);
        Bid savedBid = bidRepository.save(bid);

        // Get highest bid for this auction
        BigDecimal highestBid = bidRepository.findByAuction(auction)
                .stream()
                .map(Bid::getAmount)
                .max(BigDecimal::compareTo)
                .orElse(amount);

        // Send confirmation email
        String subject = "Bid Confirmation - Auction #" + auction.getId();
        String body = String.format("Hi %s,\n\nYou placed a bid of ₹%s.\nCurrent highest bid: ₹%s.\n\nThanks for participating!",
                user.getName(), amount, highestBid);
        emailService.sendEmail(user.getEmail(), subject, body);

        return savedBid;
    }


    public List<BidDTO> getBidsByAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
        return bidRepository.findByAuction(auction)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Map Bid entity to BidDTO
    public BidDTO mapToDTO(Bid bid) {
        BidDTO dto = new BidDTO();
        dto.setId(bid.getId());
        dto.setUserId(bid.getUser().getId());
        dto.setAuctionId(bid.getAuction().getId());
        dto.setAmount(bid.getAmount());
        dto.setBidTime(bid.getBidTime());
        return dto;
    }

    // Get bids placed by a user
    public List<BidDTO> getBidsByUser(User user) {
        return bidRepository.findByUser(user)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get all bids
    public List<BidDTO> getAllBids() {
        return bidRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

}
