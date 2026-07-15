package com.jvlcode.spring_boot_demo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    //Secret key Generation
    private static final String SECREAT_KEY_STRING= "LJ1zqpJBFZ3LMHmKvcfnBADTDDy0BPAn";
    private final SecretKey SECRET_KEY =Keys.hmacShaKeyFor(SECREAT_KEY_STRING.getBytes());

    //JWT token Generation
    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SECRET_KEY,Jwts.SIG.HS256)
                .compact()
                ;
    }

//JWT Validation by the Parsing (Decrypt Method)
public  String extractUsername(String token) {
     return   Jwts.parser()
                .verifyWith(SECRET_KEY)  // secret key Input
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
}
// Comparing weather Extract username and User Details are Correct
    public boolean validToken(String token, UserDetails userDetails){
        return extractUsername(token).equals((userDetails.getUsername()));
    }

}
