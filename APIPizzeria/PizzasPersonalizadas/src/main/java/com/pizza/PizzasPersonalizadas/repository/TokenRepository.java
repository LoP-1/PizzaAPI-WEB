package com.pizza.PizzasPersonalizadas.repository;

import com.pizza.PizzasPersonalizadas.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
}
