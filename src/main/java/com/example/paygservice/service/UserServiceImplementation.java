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
}
