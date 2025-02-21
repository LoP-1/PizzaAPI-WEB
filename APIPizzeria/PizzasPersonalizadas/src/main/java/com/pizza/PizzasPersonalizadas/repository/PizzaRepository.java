package com.pizza.PizzasPersonalizadas.repository;

import com.pizza.PizzasPersonalizadas.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PizzaRepository extends JpaRepository<Pizza,Long> {
    List<Pizza> findByUser_Id(Long userId);
}
