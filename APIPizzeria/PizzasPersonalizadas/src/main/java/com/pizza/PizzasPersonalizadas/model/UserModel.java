package com.pizza.PizzasPersonalizadas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(unique = true)
    private String username;

    private String password;

    @OneToMany(mappedBy = "userModel",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Token> tokens;

}
