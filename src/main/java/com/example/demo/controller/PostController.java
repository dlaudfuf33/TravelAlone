package com.example.demo.controller;

import com.example.demo.MediaProcessor;
import com.example.demo.entity.Post;
import com.example.demo.service.PostService;
import com.example.demo.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "POSTS API", description = "게시판(Posts) 관련 API 엔드포인트")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final MediaProcessor mediaProcessor;

    private final TokenService tokenService;


    // 모든 게시물 목록을 가져오는 엔드포인트
    @Operation(summary = "모든 게시물의 모든 데이터를 조회합니다.")
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


    @Operation(summary = "게시물을 생성합니다.")
    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestHeader("Authorization") String token, @RequestPart("post") String postStr) {
        try {
            String userId = "";
            if (token != null) {
                // 토큰 검증
                String jwtToken = token.substring(7); // "Bearer " 제거
                if (tokenService.isTokenBlacklisted(jwtToken)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 블랙리스트에 있습니다.");
                }
                Claims claims = tokenService.parseToken(jwtToken);
                if (claims == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 블랙리스트에 있습니다.");
                }
                userId = (String) claims.get("sub");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Post post = objectMapper.readValue(postStr, Post.class);

            // 작성자명과 비밀번호로 회원 여부 확인
            if (post.getAuthor() != null && post.getPassword() != null) {
                // 비회원인 경우
                // 비밀번호를 설정하고 userId를 null로 설정
                post.setUserId("");

                // 게시물 생성
                Post createdPost = postService.createPost(post);

                if (post.getContents() != null) {
                    // HTML 파싱 및 이미지 처리
                    Document document = Jsoup.parse(post.getContents());
                    mediaProcessor.processMediaPost(document, createdPost);

                    // 변경된 HTML을 다시 게시물에 설정
                    String updatedContent = document.outerHtml();
                    createdPost.setContents(updatedContent);
                    postService.updatePost(createdPost.getId(), createdPost);
                }

                System.out.println("비회원 게시물 생성: ID " + createdPost.getId() + "의 게시물이 생성됨");
                return ResponseEntity.ok(createdPost);
            } else if (post.getUserId() != null) {
                // 회원인 경우
                // userId를 작성자로 설정하고 비밀번호를 null로 설정
                post.setAuthor(userId);
                post.setUserId(userId);
                post.setPassword(null);

                // 게시물 생성
                Post createdPost = postService.createPost(post);

                if (post.getContents() != null) {
                    // HTML 파싱 및 이미지 처리
                    Document document = Jsoup.parse(post.getContents());
                    mediaProcessor.processMediaPost(document, createdPost);

                    // 변경된 HTML을 다시 게시물에 설정
                    String updatedContent = document.outerHtml();
                    createdPost.setContents(updatedContent);
                    postService.updatePost(createdPost.getId(), createdPost);
                }

                System.out.println("회원 게시물 생성: ID " + createdPost.getId() + "의 게시물이 생성됨");
                return ResponseEntity.ok(createdPost);
            } else {
                // 비회원 및 회원이 아닌 경우에는 적절한 오류 응답을 반환
                return ResponseEntity.badRequest().body("작성자명 또는 비밀번호가 누락되었습니다.");
            }
        } catch (Exception e) {
            System.err.println("게시물 생성 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // 특정 ID의 게시물을 가져오는 엔드포인트
    @Operation(summary = "특정 ID의 게시물을 가져옵니다.")
    @GetMapping("/view/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        try {
            Post post = postService.getPostByIdWithFiles(id); // 게시물과 파일 정보 함께 조회
            if (post != null) {
                System.out.println("게시물 조회: ID " + id + "의 게시물이 반환됨");
                System.out.println("게시물 조회: ID " + id + "의" + post);
                return ResponseEntity.ok(post);
            } else {
                System.out.println("게시물 조회: ID " + id + "의 게시물이 없음");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("게시물 조회 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(summary = "특정 ID의 게시물을 업데이트합니다.")
    @PutMapping("/update/{id}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long id,
            @RequestPart("post") Post updatedPostData
    ) {
        try {
            // 해당 ID의 게시물을 데이터베이스에서 조회
            Post existingPost = postService.getPostById(id);

            if (existingPost != null) {
                // 업데이트된 게시물의 컨텐츠를 파싱하고 미디어 처리
                Document document = Jsoup.parse(updatedPostData.getContents());
                mediaProcessor.processMediaPost(document, existingPost);
                String updatedContent = document.outerHtml();
                existingPost.setContents(updatedContent);

                // 업데이트된 게시물 정보 설정
                existingPost.setTitle(updatedPostData.getTitle());
                existingPost.setAuthor(updatedPostData.getAuthor());

                // 게시물을 업데이트
                Post updatedPost = postService.updatePost(id, existingPost);

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


