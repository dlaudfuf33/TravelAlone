package com.example.demo.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserDestinationActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Destination destination;

    @Column(name = "start_time")
    private LocalDateTime startTime; // 페이지 접속 시작 시간

    @Column(name = "end_time")
    private LocalDateTime endTime;   // 페이지 접속 종료 시간

    @Column(name = "photo_clicked")
    private Boolean photoClicked;    // 사진 클릭 여부

    @Column(name = "scrolled")
    private Boolean scrolled;        // 스크롤 여부

    // 기본 생성자
    public UserDestinationActivity() {}

    // 생성자
    public UserDestinationActivity(User user, Destination destination, LocalDateTime startTime, LocalDateTime endTime, Boolean photoClicked,Boolean scrolled) {
        this.user = user;
        this.destination = destination;
        this.startTime = startTime;
        this.endTime = endTime;
        this.photoClicked = photoClicked;
        this.scrolled = scrolled;
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean getPhotoClicked() {
        return photoClicked;
    }

    public void setPhotoClickeded(boolean photoClicked) {
        this.photoClicked = photoClicked;
    }
    public boolean getScolled() {
        return scrolled;
    }

    public void setScolled(boolean scrolled) {
        this.scrolled = scrolled;
    }
}
