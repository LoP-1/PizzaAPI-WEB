package com.pizza.PizzasPersonalizadas.service;

import com.pizza.PizzasPersonalizadas.controller.record.LoginRequest;
import com.pizza.PizzasPersonalizadas.controller.record.RegisterRequest;
import com.pizza.PizzasPersonalizadas.controller.record.TokenResponse;
import com.pizza.PizzasPersonalizadas.model.UserModel;
import com.pizza.PizzasPersonalizadas.model.Token;
import com.pizza.PizzasPersonalizadas.repository.TokenRepository;
import com.pizza.PizzasPersonalizadas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public TokenResponse register(RegisterRequest request){
        var user = UserModel.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser,jwtToken);
        return new TokenResponse(jwtToken,refreshToken);
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

    public TokenResponse refreshToken(final String authHeader){
        return null;
    }

    public TokenResponse login(LoginRequest request) {
        return null;
    }

}
