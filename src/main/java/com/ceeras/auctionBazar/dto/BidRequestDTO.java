package com.ceeras.auctionBazar.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BidRequestDTO {
    private Long auctionId;
    private BigDecimal amount;
}