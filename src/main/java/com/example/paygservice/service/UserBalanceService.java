package com.example.paygservice.service;

import com.example.paygservice.model.UserBalance;
import java.math.BigDecimal;

public interface UserBalanceService {
    UserBalance getUserBalance(Long userId);
    UserBalance updateBalance(Long userId, BigDecimal amount);
    UserBalance deductBalance(Long userId, BigDecimal amount);
    UserBalance addBalance(Long userId, BigDecimal amount);
    boolean hasEnoughBalance(Long userId, BigDecimal amount);
} 