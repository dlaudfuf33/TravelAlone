package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {


    private final PostRepository postRepository;

    private final CommentRepository commentRepository;


    // 모든 게시물 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 특정 ID의 게시물 조회
    public Post getPostById(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        return post;
    }

    // 게시물 생성
    public Post createPost(Post post) {
        return postRepository.save(post); // Post 엔티티와 연관된 FileEntity도 함께 저장됩니다.
    }

    // 게시물 업데이트
    public Post updatePost(Long id, Post post) {
        if (postRepository.existsById(id)) {
            post.setId(id);
            return postRepository.save(post);
        }
        return null;
    }

    // 게시물 삭제
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public Post getPostByIdWithFiles(Long id) {
        try {
            // 게시물 조회
            Post post = postRepository.findById(id).orElse(null);

            return post;
        } catch (Exception e) {
            // 예외 처리 로직 추가
            throw new RuntimeException("게시물 조회 중 오류 발생: " + e.getMessage());
        }
    }

}
