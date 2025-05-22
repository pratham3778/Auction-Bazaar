package com.ceeras.auctionBazar.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BidDTO {
    private Long id;
    private Long auctionId;
    private Long userId;
    private BigDecimal amount;
    private LocalDateTime bidTime;
}