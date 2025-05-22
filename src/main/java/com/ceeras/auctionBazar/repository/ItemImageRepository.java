package com.ceeras.auctionBazar.repository;

import com.ceeras.auctionBazar.entity.ItemImage;
import com.ceeras.auctionBazar.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
    List<ItemImage> findByAuction(Auction auction);
}
