package com.yi.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_no")
    private Integer menuNo;

    @Column(name = "menu_name", length = 100)
    private String menuName;

    @Column(name = "menu_price", length = 100)
    private String menuPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_no")
    private Dinning restNo;

    // 생성자, 게터, 세터 등 필요한 부분을 추가할 수 있습니다.

    public Menu() {
    }


}