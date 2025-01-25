package com.example.paygservice.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BalanceDTO {
    private Long userId;
    private BigDecimal balance;
    private LocalDateTime lastUpdated;
} 