package com.pizza.PizzasPersonalizadas.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PizzaIngredientId implements Serializable {
    private Long pizzaId;
    private Long ingredientId;
}