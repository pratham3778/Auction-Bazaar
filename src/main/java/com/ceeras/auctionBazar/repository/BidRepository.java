package com.ceeras.auctionBazar.repository;

import com.ceeras.auctionBazar.entity.Bid;
import com.ceeras.auctionBazar.entity.Auction;
import com.ceeras.auctionBazar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByAuction(Auction auction);

    List<Bid> findByUser(User user);


}
