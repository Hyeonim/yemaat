package com.yi.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no", nullable = false)
    private Integer id;

    @Column(name = "subject", length = 30)
    private String subject;

    @Column(name = "writer", length = 20)
    private String writer;

    @Column(name = "write_date")
    private Instant writeDate;

    @Lob
    @Column(name = "content", columnDefinition = "text")
    private String content;

}