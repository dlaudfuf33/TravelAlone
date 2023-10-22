package com.example.demo.entity;


import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;

@Entity
@Table(name = "user_destinations")
public class UserDestination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Schema(description = "사용자 정보")
    private User user;

    @ManyToOne
    @JoinColumn(name = "destination_id")
    @Schema(description = "여행지 정보")
    private Destination destination;

    @Schema(description = "사용자 평점")
    private Double rating;

    @Schema(description = "리뷰")
    private String review;


    // 사용자가 평가한 여행지의 고유 ID를 반환합니다.
    public Long getId() {
        return id;
    }

    // 평점을 반환합니다.
    public Double getRating() {
        return rating;
    }

    // 리뷰 내용을 반환합니다.
    public String getReview() {
        return review;
    }

    // 사용자를 반환합니다.
    public User getUser() {
        return user;
    }

    // 여행지를 반환합니다.
    public Destination getDestination() {
        return destination;
    }

    // 사용자가 평가한 여행지의 고유 ID를 설정합니다.
    public void setId(Long id) {
        this.id = id;
    }

    // 평점을 설정합니다.
    public void setRating(Double rating) {
        this.rating = rating;
    }

    // 리뷰 내용을 설정합니다.
    public void setReview(String review) {
        this.review = review;
    }

    // 사용자를 설정합니다.
    public void setUser(User user) {
        this.user = user;
    }

    // 여행지를 설정합니다.
    public void setDestination(Destination destination) {
        this.destination = destination;
    }


}
