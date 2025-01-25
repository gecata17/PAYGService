package com.example.paygservice.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DepositRequest {
    private String paymentMethod;
    private BigDecimal amount;
} 