package com.yi.spring.entity;

import com.yi.spring.entity.DiningRest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "menu", schema = "pro_tabling")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_no", nullable = false)
    private Integer id;

    @Column(name = "menu_name", length = 100)
    private String menuName;

    @Column(name = "menu_price", length = 100)
    private String menuPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_no")
    private Dinning restNo;

}