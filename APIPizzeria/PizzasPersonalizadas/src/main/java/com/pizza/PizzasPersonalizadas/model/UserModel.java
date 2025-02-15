package com.pizza.PizzasPersonalizadas.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserModel{

    @Id
    @GeneratedValue
    private Long id;

    private String email;
    private String username;


    @Column(unique = true)
    private String password;

    @OneToMany(mappedBy = "userModel",fetch = FetchType.LAZY)
    private List<Token> tokens;

}
