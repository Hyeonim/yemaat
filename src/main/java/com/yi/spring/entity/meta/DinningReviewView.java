package com.yi.spring.entity.meta;

import com.yi.spring.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "dinning_review_view")
public class DinningReviewView {
    @Id
    @Column(name = "rest_no")
    private Integer restNo;

    @Size(max = 255)
    @Column(name = "rest_name")
    private String restName;

    @Size(max = 255)
    @Column(name = "rest_addr")
    private String restAddr;

    @Size(max = 20)
    @Column(name = "rest_tel", length = 20)
    private String restTel;

    @Size(max = 50)
    @Column(name = "rest_seat", length = 50)
    private String restSeat;

    @Size(max = 255)
    @Column(name = "rest_time")
    private String restTime;

    @Size(max = 255)
    @Column(name = "rest_off_days")
    private String restOffDays;

    @Size(max = 255)
    @Column(name = "rest_parking")
    private String restParking;

    @Lob
    @Column(name = "rest_menu")
    private String restMenu;

    @Size(max = 255)
    @Column(name = "rest_category")
    private String restCategory;

    @Column(name = "rest_latitude")
    private Double restLatitude;

    @Column(name = "rest_longitude")
    private Double restLongitude;

    @Size(max = 255)
    @Column(name = "rest_img")
    private String restImg;

    @Size(max = 100)
    @Column(name = "rest_description", length = 100)
    private String restDescription;

    @Size(max = 100)
    @Column(name = "user_id", length = 100)
    private String userId;

    @Size(max = 100)
    @Column(name = "rest_start_date", length = 100)
    private String restStartDate;

    @Size(max = 100)
    @Column(name = "rest_status", length = 100)
    private String restStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User userNo;

    @Column(name = "rest_score")
    private Float restScore;

    @Column(name = "total_reviews")
    private Long totalReviews;

    @Column(name = "rest_score2", precision = 14, scale = 4)
    private BigDecimal restScore2;

    @Column(name = "reserve_count")
    private Long reserveCount;

    public String getBase64Image() {
        return restImg;
    }

    public BigDecimal getRestScore()
    {
        return restScore2;
    }
}


//CREATE or replace view dinning_review_view as
//SELECT d.*, (SELECT COUNT(*) FROM review r  WHERE r.rest_no = d.rest_no  ) AS total_reviews
//, (SELECT avg(r.rev_score) FROM review r WHERE r.rest_no  = d.rest_no) AS rest_score2
//, (SELECT COUNT(*) FROM reservation res WHERE res.rest_no  = d.rest_no) AS reserve_count
//FROM dining_rest d ;

