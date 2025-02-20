package com.pizza.PizzasPersonalizadas.service;

import com.pizza.PizzasPersonalizadas.controller.record.LoginRequest;
import com.pizza.PizzasPersonalizadas.controller.record.RegisterRequest;
import com.pizza.PizzasPersonalizadas.controller.record.TokenResponse;
import com.pizza.PizzasPersonalizadas.model.UserModel;
import com.pizza.PizzasPersonalizadas.model.Token;
import com.pizza.PizzasPersonalizadas.repository.TokenRepository;
import com.pizza.PizzasPersonalizadas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse register(RegisterRequest request) {
        var user = UserModel.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return new TokenResponse(jwtToken, refreshToken);
    }

    private void saveUserToken(UserModel user, String jwtToken) {
        var token = Token.builder()
                .userModel(user)
                .token(jwtToken)
                .tokenType(Token.TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }


    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        var user = userRepository.findByUsername(request.username())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        revokedAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return new TokenResponse(jwtToken, refreshToken);
    }

    private void revokedAllUserTokens(final UserModel user) {
        final List<Token> validUserTokens = tokenRepository
                .findAllByUserModelIdAndExpiredFalseAndRevokedFalse(user.getId());
        if (!validUserTokens.isEmpty()) {
            for (final Token token : validUserTokens) {
                token.setExpired(true);
                token.setRevoked(true);
            }
            tokenRepository.saveAll(validUserTokens);
        }
        ;
    }

    public TokenResponse refreshToken(final String authHeader) {
        if(authHeader== null || !authHeader.startsWith("Bearer ")){
            throw new IllegalArgumentException("Token Bearer Invalido");

        }
        final String refreshToken = authHeader.substring((7));
        final String userUsername = jwtService.extractUsername(refreshToken);

        if(userUsername == null){
            throw new IllegalArgumentException("Token de renovación Invalido");
        }

        final UserModel user = userRepository.findByUsername(userUsername)
            .orElseThrow(() -> new UsernameNotFoundException(userUsername));
        if(!jwtService.isTokenValid(refreshToken, user)){
            throw new IllegalArgumentException("Token de renovacion Invalido");
        }
        final String accessToken = jwtService.generateToken(user);
        revokedAllUserTokens(user);
        saveUserToken(user, accessToken);
        return new TokenResponse(accessToken,refreshToken);
    }



}
