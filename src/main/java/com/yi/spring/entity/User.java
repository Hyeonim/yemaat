package com.yi.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@ToString
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no", nullable = false)
    private Integer id;

    @Column(name = "user_id", length = 30)
    private String userId;

    @Column(name = "user_name", length = 50)
    private String userName;

    @Column(name = "user_password", length = 50)
    private String userPassword;

    @Column(name = "user_email", length = 50)
    private String userEmail;

    @Column(name = "user_tel", length = 100)
    private String userTel;

    @Column(name = "user_auth", length = 100)
    private String userAuth;

    @Column(name = "user_start_date", length = 100)
    private String userStartDate;

    @Column(name = "user_img", length = 100)
    private String userImg;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "username")
    private String username;

    @OneToMany(mappedBy = "userNo")
    @ToString.Exclude
    private Set<Dinning> diningRests = new LinkedHashSet<>();

    @OneToMany(mappedBy = "userNo")
    @ToString.Exclude
    private Set<Review> reviews = new LinkedHashSet<>();

}