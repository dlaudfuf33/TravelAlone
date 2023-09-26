package com.example.demo.repository;

import com.example.demo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 댓글 정보를 데이터베이스에서 조작하기 위한 리포지토리 인터페이스입니다.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 추가적인 메서드가 필요한 경우 여기에 정의
}
