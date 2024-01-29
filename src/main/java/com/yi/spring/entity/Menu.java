package com.yi.spring.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="menu")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int menu_no;
    private String menu_name;
    private String menu_price;
}
