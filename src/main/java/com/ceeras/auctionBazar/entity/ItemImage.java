package com.ceeras.auctionBazar.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class    ItemImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @Column(nullable = false)
    private String title;
}
