package com.pizza.PizzasPersonalizadas.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pizza.PizzasPersonalizadas.model.Token;
import com.pizza.PizzasPersonalizadas.model.UserModel;
import com.pizza.PizzasPersonalizadas.repository.UserRepository;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
