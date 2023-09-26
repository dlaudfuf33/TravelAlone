package com.example.demo.controller;

import com.example.demo.entity.Comment;
import com.example.demo.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;



import java.util.List;

/**
 * 댓글 관련 API를 처리하는 컨트롤러입니다.
 */
@Tag(name = "Comment API", description = "댓글을 생성, 조회, 수정, 삭제하는 API")
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 모든 댓글 목록을 가져오는 엔드포인트
     *
     * @return 모든 댓글 목록
     */
    @Operation(summary = "모든 댓글을 조회합니다.")
    public ResponseEntity<List<Comment>> getAllComments() {
        try {
            List<Comment> comments = commentService.getAllComments();
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
     * 새로운 댓글을 생성하는 엔드포인트
     *
     * @param comment 새로운 댓글 정보
     * @return 생성된 댓글 정보
     */
    @Operation(summary = "새로운 댓글 생성")
    @PostMapping("/create")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        try {
            Comment createdComment = commentService.createComment(comment);
            return ResponseEntity.ok(createdComment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 특정 ID의 댓글을 조회하는 엔드포인트
     *
     * @param id 댓글 ID
     * @return 조회된 댓글 정보
     */
    @Operation(summary = "특정 ID의 댓글을 조회합니다.")
    @GetMapping("read/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        try {
            Comment comment = commentService.getCommentById(id);
            if (comment != null) {
                return ResponseEntity.ok(comment);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 특정 ID의 댓글을 수정하는 엔드포인트
     *
     * @param id      댓글 ID
     * @param comment 수정할 댓글 정보
     * @return 수정된 댓글 정보
     */
    @Operation(summary = "특정 ID의 댓글을 수정합니다.")
    @PutMapping("update/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        try {
            Comment updatedComment = commentService.updateComment(id, comment);
            if (updatedComment != null) {
                return ResponseEntity.ok(updatedComment);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 특정 ID의 댓글을 삭제하는 엔드포인트
     *
     * @param id 댓글 ID
     * @return 삭제 결과 (204 No Content)
     */
    @Operation(summary = "특정 ID의 댓글을 삭제합니다.")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
