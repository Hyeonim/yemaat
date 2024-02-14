package com.yi.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Base64;

@Getter
@Setter
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_no", nullable = false)
    private int eventNo;

    @Column(name = "event_title", length = 100)
    private String eventTitle;

    @Column(name = "event_content", length = 100)
    private String eventContent;

    @Column(name = "event_img")
    private byte[] eventImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_no")
    private Dinning restNo;

    public String getBase64Image() {
        if (eventImg != null && eventImg.length > 0) {
            return Base64.getEncoder().encodeToString(eventImg);
        }
        return "";
    }
}