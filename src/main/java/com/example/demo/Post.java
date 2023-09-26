package com.example.demo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id // 기본 키임을 명시합니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성 전략을 명시합니다.
    private Long id; // 게시글의 고유 번호입니다.

    @Column(nullable = false) // 컬럼의 NOT NULL 제약 조건을 명시합니다.
    private String title; // 게시글의 제목입니다.

    @Column(nullable = false, columnDefinition = "TEXT") // 컬럼의 타입과 NOT NULL 제약 조건을 명시합니다.
    private String content; // 게시글의 내용입니다.

    @Column(nullable = false) // 컬럼의 NOT NULL 제약 조건을 명시합니다.
    private String author; // 게시글의 작성자입니다.

    @Column // 이 필드가 컬럼임을 명시합니다.
    private Timestamp createdAt; // 게시글의 생성 시간입니다.

    @Column // 이 필드가 컬럼임을 명시합니다.
    private Timestamp updatedAt; // 게시글의 수정 시간입니다.

    @Column // 이 필드가 컬럼임을 명시합니다.
    private String imageUrl; // 게시글의 이미지 URL입니다.

    @Column // 이 필드가 컬럼임을 명시합니다.
    private String videoUrl; // 게시글의 비디오 URL입니다.

    @Column // 이 필드가 컬럼임을 명시합니다.
    private String youtubeUrl; // 게시글에 삽입된 유튜브 영상의 URL입니다.

    @Column(columnDefinition = "json") // 컬럼의 타입을 명시합니다.
    private String mapLocation; // 카카오 맵 API를 통해 선택된 위치 정보입니다. JSON 형식으로 저장됩니다.

    @Column(columnDefinition = "json") // 컬럼의 타입을 명시합니다.
    private String votes; // 투표 결과 정보입니다. JSON 형식으로 저장됩니다.

    @Column(precision = 3, scale = 2) // 컬럼의 정밀도와 스케일을 명시합니다.
    private Double averageRating; // 게시글의 평균 평점입니다.

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // 댓글 추가 메서드
    public void addComment(Comment comment) {
        comment.setPost(this);
        comments.add(comment);
    }
    // Setter 메소드들

    // 아이디 설정 메소드
    public void setId(Long id) {
        this.id = id;
    }

    // 제목 설정 메소드
    public void setTitle(String testTitle) {
        this.title = testTitle;
    }

    // 내용 설정 메소드
    public void setContent(String testContent) {
        this.content = testContent;
    }

    // 작성자 설정 메소드
    public void setAuthor(String testAuthor) {
        this.author = testAuthor;
    }

    // 생성 시간 설정 메소드
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // 수정 시간 설정 메소드
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // 이미지 URL 설정 메소드
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // 비디오 URL 설정 메소드
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    // 유튜브 URL 설정 메소드
    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    // 지도 위치 설정 메소드
    public void setMapLocation(String mapLocation) {
        this.mapLocation = mapLocation;
    }

    // 투표 설정 메소드
    public void setVotes(String votes) {
        this.votes = votes;
    }

    // 평균 평점 설정 메소드
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    // Getter 메소드들

    // 아이디 반환 메소드
    public Long getId() {
        return this.id;
    }

    // 제목 반환 메소드
    public String getTitle() {
        return this.title;
    }

    // 내용 반환 메소드
    public String getContent() {
        return this.content;
    }

    // 작성자 반환 메소드
    public String getAuthor() {
        return this.author;
    }

    // 생성 시간 반환 메소드
    public Timestamp getCreatedAt() {
        return createdAt != null ? new Timestamp(createdAt.getTime()) : null;
    }

    // 수정 시간 반환 메소드
    public Timestamp getUpdatedAt() {
        return updatedAt != null ? new Timestamp(updatedAt.getTime()) : null;
    }

    // 이미지 URL 반환 메소드
    public String getImageUrl() {
        return imageUrl;
    }

    // 비디오 URL 반환 메소드
    public String getVideoUrl() {
        return videoUrl;
    }

    // 유튜브 URL 반환 메소드
    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    // 지도 위치 반환 메소드
    public String getMapLocation() {
        return mapLocation;
    }

    // 투표 반환 메소드
    public String getVotes() {
        return votes;
    }

    // 평균 평점 반환 메소드
    public Double getAverageRating() {
        return averageRating;
    }

}