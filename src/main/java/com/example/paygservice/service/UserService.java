package com.example.paygservice.service;

import com.example.paygservice.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface UserService
{
    void createUser(User user);

    User findByUsername(String username);


    User findByEmail(String email);

    List<User> getAllUsers();

    User updateUser(User updatedUser, String username);
    void deleteByUsername(String username);

    BigDecimal updateBalance(User user, BigDecimal amount);
    BigDecimal deductBalance(User user, BigDecimal amount);
    BigDecimal addBalance(User user, BigDecimal amount);
    boolean hasEnoughBalance(User user, BigDecimal amount);
}
