package com.pizza.PizzasPersonalizadas.controller;

import com.pizza.PizzasPersonalizadas.controller.record.PizzaRequest;
import com.pizza.PizzasPersonalizadas.controller.record.PizzaResponse;
import com.pizza.PizzasPersonalizadas.service.PizzaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pizzas")
@RequiredArgsConstructor
public class PizzaController {

    private final PizzaService pizzaService;

    @PostMapping
    public ResponseEntity<PizzaResponse> createPizza(@RequestBody PizzaRequest pizzaRequest) {
        PizzaResponse pizzaResponse = pizzaService.createPizza(pizzaRequest);
        return ResponseEntity.ok(pizzaResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PizzaResponse> getPizzaById(@PathVariable Long id) {
        PizzaResponse pizzaResponse = pizzaService.getPizzaById(id);
        return ResponseEntity.ok(pizzaResponse);
    }
}

