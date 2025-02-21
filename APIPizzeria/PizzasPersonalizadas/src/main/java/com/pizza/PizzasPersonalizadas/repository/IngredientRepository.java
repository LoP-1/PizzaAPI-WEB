package com.pizza.PizzasPersonalizadas.repository;

import com.pizza.PizzasPersonalizadas.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient,Long> {
}