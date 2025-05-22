package com.ceeras.auctionBazar.controller;

import com.ceeras.auctionBazar.dto.ItemImageDTO;
import com.ceeras.auctionBazar.service.ItemImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class ItemImageController {

    private final ItemImageService itemImageService;

    public ItemImageController(ItemImageService itemImageService) {
        this.itemImageService = itemImageService;
    }

    @PostMapping("/add")
    public ResponseEntity<ItemImageDTO> addImage(@RequestParam Long auctionId, @RequestParam String title) {
        return ResponseEntity.ok(itemImageService.addImage(auctionId, title));
    }

    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<List<ItemImageDTO>> getImagesByAuction(@PathVariable Long auctionId) {
        return ResponseEntity.ok(itemImageService.getImagesByAuction(auctionId));
    }
}
