package com.yi.spring.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="UserName", nullable = false, length = 40)
    private String username;
    private String password;
    private String email;


    @Builder
    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;

    }

}
