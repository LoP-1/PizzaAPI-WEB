package com.pizza.PizzasPersonalizadas.repository;

import com.pizza.PizzasPersonalizadas.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findAllByUserModelIdAndExpiredFalseAndRevokedFalse(Long userId);
    Optional<Token> findByToken(String token);
}





