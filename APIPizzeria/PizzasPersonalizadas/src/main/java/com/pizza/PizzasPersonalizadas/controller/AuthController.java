package com.pizza.PizzasPersonalizadas.controller;

import com.pizza.PizzasPersonalizadas.controller.record.LoginRequest;
import com.pizza.PizzasPersonalizadas.controller.record.RegisterRequest;
import com.pizza.PizzasPersonalizadas.controller.record.TokenResponse;
import com.pizza.PizzasPersonalizadas.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController{

    private final AuthService service;


    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody final RegisterRequest request){
        final TokenResponse token = service.register(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> autenthicate(@RequestBody final LoginRequest request){
        final TokenResponse token = service.login(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public TokenResponse refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION)final String authHeader){
        return service.refreshToken(authHeader);
    }

}
