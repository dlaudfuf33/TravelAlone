package com.example.demo;

import javax.persistence.*;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String author;

//    Setter 메소드들
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String testTitle) {
        this.title = testTitle;
    }

    public void setContent(String testContent) {
        this.content = testContent;
    }

    public void setAuthor(String testAuthor) {
        this.author = testAuthor;
    }

//  Getter 메소드
    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public String getAuthor() {
        return this.author;
    }

    // 기본 생성자, 오버로드된 생성자 등
}