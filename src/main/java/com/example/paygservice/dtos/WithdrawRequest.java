package com.example.paygservice.dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class WithdrawRequest {
    private String paymentMethod;
    private BigDecimal amount;
} 