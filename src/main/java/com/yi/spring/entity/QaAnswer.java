package com.yi.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "qa_answer")
public class QaAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_no", nullable = false)
    private Integer id;

    @Column(name = "answer_title", length = 100)
    private String answerTitle;

    @Column(name = "answer_content", length = 100)
    private String answerContent;

    @Column(name = "qa_no")
    private Integer qaNo;

}