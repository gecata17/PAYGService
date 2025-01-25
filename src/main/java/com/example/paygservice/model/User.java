package com.example.paygservice.model;

import com.example.paygservice.config.UserDetailsImplementation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "app_user")
@Data
public class User  {

    @Id
    @Column(name = "username", nullable = false)
    String username;

    @JsonIgnore
    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "email",nullable = false)
    private String email;



    public User(String username, String email,String password) {
        this.username = username;
        this.email = email;
        this.password = password;

    }

    public User(UserDetailsImplementation userDetails){
        this.username=userDetails.getUsername();
        this.email=userDetails.getEmail();
        this.password=userDetails.getPassword();
    }

}
