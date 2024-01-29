package com.yi.spring.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="dining_rest")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiningRest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rest_no;
    private String rest_name;
    private String rest_addr;
    private String rest_tel;
    private String rest_seat;
    private String rest_time;
    private String rest_off_days;
    private String rest_parking;
    private String rest_menu;
    private String rest_category;
    private String rest_latitude;
    private String rest_longitude;
    private String rest_description;
    private int user_no;

    @Builder
    public DiningRest(String rest_name, String rest_addr, String rest_tel, String rest_seat, String rest_time,
                      String rest_off_days, String rest_parking, String rest_menu, String rest_category,
                      String rest_latitude, String rest_longitude, String rest_description) {
        this.rest_name = rest_name;
        this.rest_addr = rest_addr;
        this.rest_tel = rest_tel;
        this.rest_seat = rest_seat;
        this.rest_time = rest_time;
        this.rest_off_days = rest_off_days;
        this.rest_parking = rest_parking;
        this.rest_menu = rest_menu;
        this.rest_category = rest_category;
        this.rest_latitude = rest_latitude;
        this.rest_longitude = rest_longitude;
        this.rest_description = rest_description;
    }
}
