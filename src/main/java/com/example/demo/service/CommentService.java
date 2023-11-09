package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * 댓글 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;

    /**
     * 모든 댓글 목록을 가져옵니다.
     *
     * @return 모든 댓글 목록
     */
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    /**
     * 새로운 댓글을 생성합니다.
     *
     * @param comment 새로운 댓글 정보
     * @return 생성된 댓글 정보
     */
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

//    public Comment createCommentForPost(Long postId, Comment comment) {
//        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post not found"));
//        comment.setPost(post);
//        return commentRepository.save(comment);
//    }


    /**
     * 특정 ID의 댓글을 조회합니다.
     *
     * @param id 댓글 ID
     * @return 조회된 댓글 정보
     */
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    /**
     * 특정 ID의 댓글을 수정합니다.
     *
     * @param id      댓글 ID
     * @param comment 수정할 댓글 정보
     * @return 수정된 댓글 정보
     */
    public Comment updateComment(Long id, Comment comment) {
        Comment existingComment = commentRepository.findById(id).orElse(null);
        if (existingComment != null) {
            // 기존 댓글의 내용을 새로운 댓글 정보로 업데이트
            existingComment.setContent(comment.getContent());
            existingComment.setAuthor(comment.getAuthor());
            existingComment.setCreatedAt(comment.getCreatedAt());
            existingComment.setRating(comment.getRating());
            return commentRepository.save(existingComment);
        } else {
            return null; // 해당 ID의 댓글이 없을 경우 null 반환
        }
    }


    /**
     * 특정 ID의 댓글을 삭제합니다.
     *
     * @param id 댓글 ID
     */
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPost_Id(postId);
    }


    // 기타 비즈니스 로직 및 메서드 추가
}
