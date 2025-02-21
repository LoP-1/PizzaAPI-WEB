package com.pizza.PizzasPersonalizadas.controller.record;

import java.util.List;

public record PizzaResponse (
        Long id,
        String size,
        UserResponse user,
        List<IngredientRequest> ingredients
) {}