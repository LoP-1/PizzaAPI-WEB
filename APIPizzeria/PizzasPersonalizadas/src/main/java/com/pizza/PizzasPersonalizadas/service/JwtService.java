package com.pizza.PizzasPersonalizadas.service;

import com.pizza.PizzasPersonalizadas.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;


    public String generateToken(final UserModel user) {
        return buildToken(user, jwtExpiration);
    }

    public String generateRefreshToken(final UserModel user) {
        return buildToken(user, refreshExpiration);
    }

    private String buildToken(final UserModel user, final long expiration) {
        return Jwts.builder()
                .id(user.getId().toString())
                .claims(Map.of("email", user.getEmail()))
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey(){
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(final String token){
        final Claims jwtToken = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return jwtToken.getSubject();
    }

    private SecretKey getSignInKey() {
        byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public boolean isTokenValid(final String token, final UserModel user) {
        final String username = extractUsername(token);
        System.out.println("Extracted Username: " + username);
        System.out.println("Expected Username: " + user.getUsername());
        System.out.println("Token Expired: " + isTokenExpired(token));
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(final String token) {
    return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        final Claims jwtToken = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return jwtToken.getExpiration();
    }
}
