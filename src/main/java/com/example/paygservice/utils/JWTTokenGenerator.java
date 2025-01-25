package com.example.paygservice.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTTokenGenerator {

    //private key for generating jwt tokens

    private static final Key jwtSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode("exN9IOG6w0Bo8Mg7e6Y700R3Rt6ZvQ2TBocIKNpzLnev80BsA6bGmr0T6cXSFWuqexN9IOG6w0Bo8Mg7e6Y700R3Rt6ZvQ2TBocIKNpzLnev80BsA6bGmr0T6cXSFWuq"));


    public String generateToken(Authentication auth){
        UserDetails userDto = (UserDetails) auth.getPrincipal();

        int expirationTime = 86400000;
        return Jwts.builder().setSubject(userDto.getUsername())
                .setIssuedAt(new Date()).setExpiration(new Date(new Date().getTime() + expirationTime))
                .signWith(jwtSecret, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token).getBody().getSubject();
            return true;
        }catch (Exception exception){
            System.out.println("Invalid jwt token");
        }

        return false;

    }

    public String getUsernameFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token).getBody().getSubject();
    }
}
