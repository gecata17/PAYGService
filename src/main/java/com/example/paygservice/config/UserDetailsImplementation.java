package com.example.paygservice.config;

import com.example.paygservice.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;

public class UserDetailsImplementation  implements UserDetails {
    private final String username;
    private final String email;
    private final BigDecimal balance;

    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImplementation(Long id,String username, String email, String password, BigDecimal balance,Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.balance=balance;
        this.authorities=authorities;
    }

    public UserDetailsImplementation(User user){
        this.username=user.getUsername();
        this.email=user.getEmail();
        this.password=user.getPassword();
        this.balance=user.getBalance();
        this.authorities= Collections.emptyList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getEmail(){
        return email;
    }

    public BigDecimal getBalance() { return balance; }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}