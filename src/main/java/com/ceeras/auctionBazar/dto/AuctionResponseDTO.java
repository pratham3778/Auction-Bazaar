package com.ceeras.auctionBazar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AuctionResponseDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal startingPrice;
    private LocalDateTime createdAt;
    private UserDTO creator;
}
