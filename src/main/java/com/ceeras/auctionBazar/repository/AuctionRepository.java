package com.ceeras.auctionBazar.repository;

import com.ceeras.auctionBazar.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByCreatorId(Long creatorId);
}
