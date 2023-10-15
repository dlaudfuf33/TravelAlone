package com.example.demo;

import com.example.demo.entity.Post;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @BeforeEach
    public void setUp() {
        Post post = new Post();
        post.setTitle("Test Title");
        post.setContents("Test Content");
        post.setAuthor("Test Author");

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);
    }

    @Test
    public void testGetPostById() {
        Post post = postService.getPostById(1L);
        assertNotNull(post);
        assertEquals("Test Title", post.getTitle());
    }

    @Test
    public void testCreatePost() {
        Post post = new Post();
        post.setTitle("New Title");
        post.setContents("New Content");
        post.setAuthor("New Author");

        Post savedPost = postService.createPost(post);
        assertNotNull(savedPost);
        assertEquals("New Title", savedPost.getTitle());
    }

    // 다른 메서드에 대한 테스트 케이스를 추가 ...

}
