package com.example.paygservice.service;

import com.example.paygservice.dto.BalanceDTO;
import com.example.paygservice.dto.DepositRequest;
import com.example.paygservice.dto.WithdrawRequest;
import com.example.paygservice.model.*;
import com.example.paygservice.repository.TransactionRepository;
import com.example.paygservice.repository.UserBalanceRepository;
import com.example.paygservice.exception.PaymentProcessingException;
import com.example.paygservice.exception.InsufficientBalanceException;
import com.example.paygservice.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class TokenService {
    private final UserBalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentGatewayService paymentGatewayService;
    
    @Transactional
    public BalanceDTO deposit(Long userId, DepositRequest request) {
        // Process payment through payment gateway
        PaymentResult paymentResult = paymentGatewayService.processPayment(
            request.getPaymentMethod(),
            request.getAmount()
        );
        
        if (!paymentResult.isSuccessful()) {
            throw new PaymentProcessingException("Payment failed: " + paymentResult.getMessage());
        }
        
        // Update balance
        UserBalance balance = balanceRepository.findByUserId(userId)
            .orElse(new UserBalance(userId));
        balance.setBalance(balance.getBalance().add(request.getAmount()));
        balance.setUpdatedAt(LocalDateTime.now());
        balanceRepository.save(balance);
        
        // Create transaction record
        Transaction transaction = Transaction.builder()
            .userId(userId)
            .type(TransactionType.DEPOSIT)
            .amount(request.getAmount())
            .status(TransactionStatus.COMPLETED)
            .createdAt(LocalDateTime.now())
            .build();
        transactionRepository.save(transaction);
        
        return BalanceDTO.builder()
            .userId(userId)
            .balance(balance.getBalance())
            .lastUpdated(balance.getUpdatedAt())
            .build();
    }
    
    @Transactional
    public BalanceDTO withdraw(Long userId, WithdrawRequest request) {
        // Validate balance
        UserBalance balance = balanceRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Balance not found"));
            
        if (balance.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal");
        }
        
        // Process withdrawal through payment gateway
        PaymentResult withdrawalResult = paymentGatewayService.processWithdrawal(
            request.getPaymentMethod(),
            request.getAmount()
        );
        
        if (!withdrawalResult.isSuccessful()) {
            throw new PaymentProcessingException("Withdrawal failed: " + withdrawalResult.getMessage());
        }
        
        // Update balance
        balance.setBalance(balance.getBalance().subtract(request.getAmount()));
        balance.setUpdatedAt(LocalDateTime.now());
        balanceRepository.save(balance);
        
        // Create transaction record
        Transaction transaction = Transaction.builder()
            .userId(userId)
            .type(TransactionType.WITHDRAW)
            .amount(request.getAmount())
            .status(TransactionStatus.COMPLETED)
            .createdAt(LocalDateTime.now())
            .build();
        transactionRepository.save(transaction);
        
        return BalanceDTO.builder()
            .userId(userId)
            .balance(balance.getBalance())
            .lastUpdated(balance.getUpdatedAt())
            .build();
    }
} 