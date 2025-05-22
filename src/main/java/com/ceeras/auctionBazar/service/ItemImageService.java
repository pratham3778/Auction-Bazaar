package com.ceeras.auctionBazar.service;

import com.ceeras.auctionBazar.dto.ItemImageDTO;
import com.ceeras.auctionBazar.entity.Auction;
import com.ceeras.auctionBazar.entity.ItemImage;
import com.ceeras.auctionBazar.repository.AuctionRepository;
import com.ceeras.auctionBazar.repository.ItemImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemImageService {

    private final ItemImageRepository itemImageRepository;
    private final AuctionRepository auctionRepository;

    public ItemImageService(ItemImageRepository itemImageRepository, AuctionRepository auctionRepository) {
        this.itemImageRepository = itemImageRepository;
        this.auctionRepository = auctionRepository;
    }

    public ItemImageDTO addImage(Long auctionId, String title) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        ItemImage image = new ItemImage();
        image.setAuction(auction);
        image.setTitle(title);
        ItemImage saved = itemImageRepository.save(image);

        return mapToDto(saved);
    }

    public List<ItemImageDTO> getImagesByAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        return itemImageRepository.findByAuction(auction)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ItemImageDTO mapToDto(ItemImage image) {
        ItemImageDTO dto = new ItemImageDTO();
        dto.setId(image.getId());
        dto.setAuctionId(image.getAuction().getId());
        dto.setTitle(image.getTitle());
        return dto;
    }
}
