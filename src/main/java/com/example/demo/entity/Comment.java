package com.example.demo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "댓글 정보")
@Entity
@Table(name = "posts")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String author;

    @Column
    private Timestamp createdAt;

    @Column(precision = 3, scale = 2)
    private Double rating;


// Setter 메서드

    /**
     * 댓글의 내용을 설정합니다.
     *
     * @param content 댓글 내용
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 댓글을 작성한 사용자의 이름을 설정합니다.
     *
     * @param author 작성자 이름
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 댓글이 작성된 시간을 설정합니다.
     *
     * @param createdAt 댓글 작성 시간
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 댓글의 평점(rating)을 설정합니다.
     *
     * @param rating 댓글 평점
     */
    public void setRating(Double rating) {
        this.rating = rating;
    }

    // Getter 메서드
    /**
     * 댓글의 고유 식별자(ID)를 반환합니다.
     *
     * @return 댓글 ID
     */
    @Schema(description = "댓글 ID")
    public Long getId() {
        return id;
    }

    /**
     * 이 댓글이 속한 게시물(Post)을 반환합니다.
     *
     * @return 댓글이 속한 게시물
     */
    @Schema(description = "댓글이 속한 게시물")
    public Post getPost() {
        return post;
    }

    /**
     * 댓글의 내용을 반환합니다.
     *
     * @return 댓글 내용
     */
    @Schema(description = "댓글 내용")
    public String getContent() {
        return content;
    }

    /**
     * 댓글을 작성한 사용자의 이름을 반환합니다.
     *
     * @return 작성자 이름
     */
    @Schema(description = "댓글 작성자")
    public String getAuthor() {
        return author;
    }

    /**
     * 댓글이 작성된 시간을 반환합니다.
     *
     * @return 댓글 작성 시간
     */
    @Schema(description = "댓글 작성 시간")
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * 댓글의 평점(rating)을 반환합니다.
     *
     * @return 댓글 평점
     */
    @Schema(description = "댓글 평점")
    public Double getRating() {
        return rating;
    }

    // 게시물과 댓글 연결 메서드
    public void setPost(Post post) {
        this.post = post;
    }
}

