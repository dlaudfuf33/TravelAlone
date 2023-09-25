package com.example.demo;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;
    // 모든 게시물 목록을 가져오는 엔드포인트
    @Operation(summary = "모든 게시물을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        try {
            List<Post> posts = postService.getAllPosts();
            System.out.println("모든 게시물 조회: " + posts.size() + "개의 게시물이 반환됨");
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            System.err.println("모든 게시물 조회 중 오류 발생: " + e.getMessage());
            // 예외 발생 시 500 Internal Server Error를 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 특정 ID의 게시물을 가져오는 엔드포인트
    @Operation(summary = "특정 ID의 게시물을 조회합니다.")
    @GetMapping("/list/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        try {
            Post post = postService.getPostById(id);
            if (post != null) {
                System.out.println("게시물 조회: ID " + id + "의 게시물이 반환됨");
                return ResponseEntity.ok(post);
            } else {
                System.out.println("게시물 조회: ID " + id + "의 게시물이 없음");
                // 해당 ID의 게시물이 없을 경우 404 Not Found를 반환
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("게시물 조회 중 오류 발생: " + e.getMessage());
            // 예외 발생 시 500 Internal Server Error를 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 새로운 게시물을 생성하는 엔드포인트
    @Operation(summary = "새로운 게시물을 생성합니다.")
    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        try {
            Post createdPost = postService.createPost(post);
            System.out.println("게시물 생성: ID " + createdPost.getId() + "의 게시물이 생성됨");
            return ResponseEntity.ok(createdPost);
        } catch (Exception e) {
            System.err.println("게시물 생성 중 오류 발생: " + e.getMessage());
            // 예외 발생 시 500 Internal Server Error를 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 특정 ID의 게시물을 업데이트하는 엔드포인트
    @Operation(summary = "특정 ID의 게시물을 업데이트합니다.")
    @PutMapping("/view/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post post) {
        try {
            Post updatedPost = postService.updatePost(id, post);
            if (updatedPost != null) {
                return ResponseEntity.ok(updatedPost);
            } else {
                // 해당 ID의 게시물이 없을 경우 404 Not Found를 반환
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // 예외 발생 시 500 Internal Server Error를 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 특정 ID의 게시물을 삭제하는 엔드포인트
    @Operation(summary = "특정 ID의 게시물을 삭제합니다.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            // 성공적으로 삭제되면 204 No Content를 반환
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // 예외 발생 시 500 Internal Server Error를 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


