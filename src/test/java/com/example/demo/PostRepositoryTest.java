package com.example.demo;

import com.example.demo.entity.Post;
import com.example.demo.repository.PostRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    public void testConnection() {
        Post post = new Post();
        post.setTitle("Test Title");
        post.setContent("Test Content");
        post.setAuthor("Test Author");

        Post savedPost = postRepository.save(post);
        assertNotNull(savedPost);
        assertNotNull(savedPost.getId());
    }
}

