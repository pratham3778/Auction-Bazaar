package com.ceeras.auctionBazar.service;

import com.ceeras.auctionBazar.dto.AuctionResponseDTO;
import com.ceeras.auctionBazar.dto.BidDTO;
import com.ceeras.auctionBazar.dto.UserDTO;
import com.ceeras.auctionBazar.entity.Auction;
import com.ceeras.auctionBazar.entity.Bid;
import com.ceeras.auctionBazar.entity.User;
import com.ceeras.auctionBazar.repository.AuctionRepository;
import com.ceeras.auctionBazar.repository.BidRepository;
import com.ceeras.auctionBazar.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final BidRepository bidRepository;
    private final BidService bidService;

    public AuctionService(AuctionRepository auctionRepository,
                          UserRepository userRepository,
                          BidRepository bidRepository,
                          BidService bidService) {
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
        this.bidRepository = bidRepository;
        this.bidService = bidService;
    }

    public Auction createAuction(Long userId, String title, String description,
                                 LocalDateTime startTime, LocalDateTime endTime,
                                 BigDecimal startingPrice) {
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

    public Auction updateAuction(Long auctionId, String title, String description,
                                 LocalDateTime startTime, LocalDateTime endTime,
                                 BigDecimal startingPrice) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        auction.setTitle(title);
        auction.setDescription(description);
        auction.setStartTime(startTime);
        auction.setEndTime(endTime);
        auction.setStartingPrice(startingPrice);
        auction.setUpdatedAt(LocalDateTime.now());

        return auctionRepository.save(auction);
    }

    public void deleteAuction(Long auctionId) {
        if (!auctionRepository.existsById(auctionId)) {
            throw new RuntimeException("Auction not found");
        }
        auctionRepository.deleteById(auctionId);
    }

    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }

    public Auction getAuctionById(Long id) {
        return auctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
    }

    public List<Auction> getAuctionsByCreator(Long creatorId) {
        return auctionRepository.findByCreatorId(creatorId);
    }

    // Map Auction entity to AuctionResponseDTO
    public AuctionResponseDTO mapToDto(Auction auction) {
        User user = auction.getCreator();
        UserDTO userDTO = new UserDTO(user.getId(), user.getEmail(), user.getName(), user.getRole());

        return new AuctionResponseDTO(
                auction.getId(),
                auction.getTitle(),
                auction.getDescription(),
                auction.getStartingPrice(),
                auction.getCreatedAt(),
                userDTO
        );
    }

    public List<AuctionResponseDTO> getAllAuctionDTOs() {
        return auctionRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public AuctionResponseDTO getAuctionDTOById(Long id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
        return mapToDto(auction);
    }

    public List<AuctionResponseDTO> getAuctionsByUserId(Long userId) {
        return auctionRepository.findByCreatorId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


}
