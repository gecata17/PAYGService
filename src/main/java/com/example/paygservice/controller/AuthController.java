package com.example.paygservice.controller;

import com.example.paygservice.config.UserDetailsImplementation;
import com.example.paygservice.dtos.LoginRequest;
import com.example.paygservice.dtos.LoginResponse;
import com.example.paygservice.dtos.SignUpRequest;
import com.example.paygservice.model.User;
import com.example.paygservice.service.UserService;
import com.example.paygservice.utils.JWTTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTTokenGenerator jwtTokenGenerator;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest) {
        User newUser = new User(signUpRequest.getUsername() ,signUpRequest.getEmail(),signUpRequest.getPassword());
        try{
            userService.createUser(newUser);
        }catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.ok().body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = jwtTokenGenerator.generateToken(auth);
        UserDetailsImplementation userDetails= (UserDetailsImplementation) auth.getPrincipal();
        return ResponseEntity.ok(new LoginResponse(jwt, userDetails.getUsername(), userDetails.getEmail()));
    }
}
