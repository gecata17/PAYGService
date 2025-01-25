package com.example.paygservice.controller;

import com.example.paygservice.model.User;
import com.example.paygservice.service.UserService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/private/api/users")
@RestController
public class UserController {

    private final UserService userService;
    private final UserDetailsService userDetails;

    @GetMapping(path="all")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{username}")
    public void deleteUser(@PathVariable("username") String username){
        userService.deleteByUsername(username);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username){
        User user= userService.findByUsername(username);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<User> updateUser(@PathVariable("username") String username,@RequestBody User user){
        try{
            return new ResponseEntity<>(userService.updateUser(user,username),HttpStatus.OK);

        } catch (EntityExistsException | IllegalArgumentException exception){
            return ResponseEntity.badRequest().build();
        }
    }


}
