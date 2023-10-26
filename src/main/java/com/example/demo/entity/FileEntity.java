//package com.example.demo.entity;
//
//import org.springframework.lang.Nullable;
//
//import javax.persistence.*;
//
//@Entity  // JPA 엔터티로 선언합니다. 이 어노테이션을 사용하여 이 클래스를 데이터베이스 테이블과 매핑합니다.
////@Table(name = 'Files')
//public class FileEntity {
//
//    @Id  // 이 필드를 기본 키로 지정합니다.
//    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 기본 키 생성 전략을 지정합니다. 여기서는 자동 증가 전략을 사용합니다.
//    private Long id;  // 파일의 고유 식별자입니다.
//
//    private String fileUrl;  // 파일의 URL을 저장할 필드입니다.
//
//    @ManyToOne  // 다대일 관계를 정의합니다. 여러 파일이 하나의 게시글에 속할 수 있습니다.
//    @JoinColumn(name = "post_id")  // 외래 키로 사용할 필드를 지정합니다.
//    private Post post;  // 이 파일이 속한 게시글을 참조합니다.
//
//    // Getter 메소드들
//    public Long getId() {
//        return id;
//    }
//
//    public String getFileUrl() {
//        return fileUrl;
//    }
//
//    // Setter 메소드들
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    // 파일 URL을 설정하는 메소드입니다.
//    public void setFileUrl(String fileUrl) {
//        this.fileUrl = fileUrl;
//    }
//
//    public void setPost(Post post) {
//        this.post =post;
//    }
//}
