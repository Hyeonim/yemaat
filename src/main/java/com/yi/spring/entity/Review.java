package com.yi.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rev_no", nullable = false)
    private Integer id;

    @Column(name = "rev_score")
    private Integer revScore;

    @Column(name = "rev_content", length = 100)
    private String revContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User userNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_no")
    private Dinning restNo;

    @Column(name = "rev_write_time", length = 100)
    private String revWriteTime;

    @Column(name = "rev_img")
    private byte[] revImg;

    @Column(name = "rev_like", length = 100)
    private String revLike;


    public double getRevScore(){

        return revScore;
    }
}