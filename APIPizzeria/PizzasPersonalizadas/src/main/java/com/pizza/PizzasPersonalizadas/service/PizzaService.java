package com.pizza.PizzasPersonalizadas.service;

import com.pizza.PizzasPersonalizadas.controller.record.IngredientRequest;
import com.pizza.PizzasPersonalizadas.controller.record.PizzaRequest;
import com.pizza.PizzasPersonalizadas.controller.record.PizzaResponse;
import com.pizza.PizzasPersonalizadas.controller.record.UserResponse;
import com.pizza.PizzasPersonalizadas.model.*;
import com.pizza.PizzasPersonalizadas.repository.IngredientRepository;
import com.pizza.PizzasPersonalizadas.repository.PizzaRepository;
import com.pizza.PizzasPersonalizadas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PizzaService {

    private final PizzaRepository pizzaRepository;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;

    public PizzaResponse createPizza(PizzaRequest pizzaRequest) {
        // Buscar el usuario por ID
        UserModel user = userRepository.findById(pizzaRequest.userId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear la pizza con una lista vacía de ingredientes
        Pizza pizza = Pizza.builder()
                .size(pizzaRequest.size())
                .user(user)
                .ingredients(new ArrayList<>()) // ✅ Usamos una lista en lugar de un HashMap
                .build();

        // Agregar los ingredientes a la pizza
        for (IngredientRequest ingredientReq : pizzaRequest.ingredients()) {
            Ingredient ingredient = ingredientRepository.findById(ingredientReq.id())
                    .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));

            // Crear la relación PizzaIngredient
            PizzaIngredient pizzaIngredient = PizzaIngredient.builder()
                    .id(new PizzaIngredientId(pizza.getId(), ingredient.getId()))
                    .pizza(pizza)
                    .ingredient(ingredient)
                    .quantity(ingredientReq.quantity())
                    .build();

            pizza.getIngredients().add(pizzaIngredient); // ✅ Agregar a la lista
        }

        // Guardar la pizza con los ingredientes en la base de datos
        pizza = pizzaRepository.save(pizza);

        // Devolver la respuesta con los datos de la pizza creada
        return new PizzaResponse(
                pizza.getId(),
                pizza.getSize(),
                new UserResponse(pizza.getUser().getId(), pizza.getUser().getUsername(), pizza.getUser().getEmail()),
                pizza.getIngredients().stream()
                        .map(pizzaIngredient -> new IngredientRequest(
                                pizzaIngredient.getIngredient().getId(),
                                pizzaIngredient.getQuantity()
                        ))
                        .collect(Collectors.toList())
        );
    }

    public PizzaResponse getPizzaById(Long id) {
        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pizza no encontrada"));

        return new PizzaResponse(
                pizza.getId(),
                pizza.getSize(),
                new UserResponse(pizza.getUser().getId(), pizza.getUser().getUsername(), pizza.getUser().getEmail()),
                pizza.getIngredients().stream()
                        .map(pizzaIngredient -> new IngredientRequest(
                                pizzaIngredient.getIngredient().getId(),
                                pizzaIngredient.getQuantity()
                        ))
                        .collect(Collectors.toList())
        );
    }
}
