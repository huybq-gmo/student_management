package com.huybq.student_management.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huybq.student_management.jwt.Token;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;
    @Column(length = 20, nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> tokens =new ArrayList<>();

    public User(int userId, String username, String password1) {
    }
}