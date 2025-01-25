package com.example.paygservice.service;

import com.example.paygservice.model.UserBalance;
import com.example.paygservice.repository.UserBalanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class UserBalanceServiceImpl implements UserBalanceService {
    
    private final UserBalanceRepository userBalanceRepository;

    public UserBalanceServiceImpl(UserBalanceRepository userBalanceRepository) {
        this.userBalanceRepository = userBalanceRepository;
    }

    @Override
    public UserBalance getUserBalance(Long userId) {
        return userBalanceRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User balance not found"));
    }

    @Override
    @Transactional
    public UserBalance updateBalance(Long userId, BigDecimal amount) {
        UserBalance balance = getUserBalance(userId);
        balance.setBalance(amount);
        return userBalanceRepository.save(balance);
    }

    @Override
    @Transactional
    public UserBalance deductBalance(Long userId, BigDecimal amount) {
        UserBalance balance = getUserBalance(userId);
        if (balance.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        balance.setBalance(balance.getBalance().subtract(amount));
        return userBalanceRepository.save(balance);
    }

    @Override
    @Transactional
    public UserBalance addBalance(Long userId, BigDecimal amount) {
        UserBalance balance = getUserBalance(userId);
        balance.setBalance(balance.getBalance().add(amount));
        return userBalanceRepository.save(balance);
    }

    @Override
    public boolean hasEnoughBalance(Long userId, BigDecimal amount) {
        UserBalance balance = getUserBalance(userId);
        return balance.getBalance().compareTo(amount) >= 0;
    }
} 