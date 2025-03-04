package com.example.paygservice.service;

import com.example.paygservice.config.UserDetailsImplementation;
import com.example.paygservice.model.User;
import com.example.paygservice.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public void createUser(User user) {

        if (userRepository.existsById(user.getUsername())) {
            throw new IllegalArgumentException("Username already in use");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already in use");
        }

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(newUser);

    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findById(username).orElseThrow(EntityExistsException::new);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }

    @Override
    public User updateUser(User updatedUser, String username) {

        if (!username.equals(updatedUser.getUsername()) && userRepository.existsById(updatedUser.getUsername())) {
            throw new EntityExistsException("Username already in use");
        }


        User existingUser = userRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));


        existingUser.setEmail(updatedUser.getEmail());


        return userRepository.save(existingUser);
    }


    @Override
    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username).orElseThrow(EntityExistsException::new);
        System.out.println(user.toString());
        return new UserDetailsImplementation(user);
    }

    @Override
    @Transactional
    public BigDecimal updateBalance(User user, BigDecimal amount) {
        user.setBalance(amount);  // Update balance with the given amount
        userRepository.save(user);  // Save the updated user
        return user.getBalance();  // Return the updated balance
    }

    @Override
    @Transactional
    public BigDecimal deductBalance(User user, BigDecimal amount) {
        if (user.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        user.setBalance(user.getBalance().subtract(amount));  // Deduct amount
        userRepository.save(user);  // Save the updated user
        return user.getBalance();  // Return the updated balance
    }

    @Override
    @Transactional
    public BigDecimal addBalance(User user, BigDecimal amount) {
        user.setBalance(user.getBalance().add(amount));  // Add amount to the balance
        userRepository.save(user);  // Save the updated user
        return user.getBalance();  // Return the updated balance
    }

    @Override
    public boolean hasEnoughBalance(User user, BigDecimal amount) {
        return user.getBalance().compareTo(amount) >= 0;  // Check if the user has enough balance
    }
}
