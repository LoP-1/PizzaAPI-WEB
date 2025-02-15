package com.pizza.PizzasPersonalizadas.repository;

import com.pizza.PizzasPersonalizadas.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

}
