package com.pizza.PizzasPersonalizadas.controller.record;

import java.util.List;

public record PizzaRequest (
        String size,
        List<IngredientRequest> ingredients,
        Long userId
){}