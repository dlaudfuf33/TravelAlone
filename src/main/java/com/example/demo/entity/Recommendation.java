package com.example.demo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;


@Entity
@Table(name = "recommendations")
public class Recommendation {

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

    @Schema(description = "추천 점수")
    private Double score;


    // 추천의 고유 ID를 반환합니다.
    public Long getId() {
        return id;
    }

    // 추천 점수를 반환합니다.
    public Double getScore() {
        return score;
    }

    // 사용자를 반환합니다.
    public User getUser() {
        return user;
    }

    // 여행지를 반환합니다.
    public Destination getDestination() {
        return destination;
    }

    // 추천의 고유 ID를 설정합니다.
    public void setId(Long id) {
        this.id = id;
    }

    // 추천 점수를 설정합니다.
    public void setScore(Double score) {
        this.score = score;
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