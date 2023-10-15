package com.example.demo.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Column(nullable = false, columnDefinition = "LONGTEXT") // 컬럼의 타입과 NOT NULL 제약 조건을 명시합니다.
    private String contents; // 게시글의 내용입니다.

    @Column(nullable = false) // 컬럼의 NOT NULL 제약 조건을 명시합니다.
    private String author; // 게시글의 작성자입니다.

    @Column // 이 필드가 컬럼임을 명시합니다.
    @CreationTimestamp
    private Timestamp createdAt; // 게시글의 생성 시간입니다.

    @Column // 이 필드가 컬럼임을 명시합니다.
    @UpdateTimestamp
    private Timestamp updatedAt; // 게시글의 수정 시간입니다.

    @Column // 이 필드가 컬럼임을 명시합니다.
    private String youtubeUrl; // 게시글에 삽입된 유튜브 영상의 URL입니다.

    @Column(columnDefinition = "json") // 컬럼의 타입을 명시합니다.
    private String mapLocation; // 카카오 맵 API를 통해 선택된 위치 정보입니다. JSON 형식으로 저장됩니다.

    @Column(columnDefinition = "json") // 컬럼의 타입을 명시합니다.
    private String votes; // 투표 결과 정보입니다. JSON 형식으로 저장됩니다.

    @Column(precision = 3, scale = 2) // 컬럼의 정밀도와 스케일을 명시합니다.
    private Double averageRating; // 게시글의 평균 평점입니다.

    @Column(nullable = true)
    private String password; // 비회원의 비밀번호 필드 추가

    @Lob  // 큰 문자열을 저장할 수 있도록 Lob 어노테이션을 사용합니다.
    private String fileUrls;  // 파일 URL들을 저장할 필드입니다.


    // Post 엔터티와 Comment 엔터티 간의 관계를 정의합니다.
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // Post 엔터티와 File 엔터티 간의 관계를 정의합니다.
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileEntity> fileEntities = new ArrayList<>();


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
    public void setContents(String testContent) {
        this.contents = testContent;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFileUrls(String fileUrls) {
        this.fileUrls = fileUrls;
    }

    //  Getter 메소드들 ###############################################################

    // 아이디 반환 메소드
    public Long getId() {
        return this.id;
    }

    // 제목 반환 메소드
    public String getTitle() {
        return this.title;
    }

    // 내용 반환 메소드
    public String getContents() {
        return this.contents;
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
    // 비회원 비밀번호 반환
    public String getPassword() {return password;}

    public String getFileUrls() {return fileUrls;}

    public <E> List<FileEntity> getFileEntities() {return fileEntities;}

    public void setFileEntities(List<FileEntity> fileEntities) {
        this.fileEntities = fileEntities;
    }
}