package com.yi.spring.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@ToString
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "Username", nullable = false, length = 40)
    private String username;
    private String password;
    private String email;

    @Builder
    User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

}
