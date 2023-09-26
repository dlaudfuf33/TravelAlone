package com.example.demo;

import com.example.demo.entity.Post;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository; // 레포지토리 빈 주입
    @BeforeEach
    public void setUp() {
        // 테스트를 진행하기 전, 기존의 테스트 데이터를 모두 삭제한다.
        // 이렇게 해야 각 테스트가 독립적으로 실행될 수 있다.
        postRepository.deleteAll();

        List<Post> posts = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            // 새로운 Post 객체를 생성한다.
            Post post = new Post();

            // Post 객체의 필드를 설정한다.
            // 여기서 i 값을 이용하여 각 필드의 값이 서로 다르게 설정된다.
            post.setTitle("Test Title " + i);
            post.setContent("Test Content " + i);
            post.setAuthor("Test Author " + i);

            // 생성 시간과 수정 시간을 현재 시간으로 설정한다.
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            post.setCreatedAt(currentTime);
            post.setUpdatedAt(currentTime);

            // 비트 연산을 통해 i의 각 비트를 확인하고,
            // 해당 비트가 1이면 각 필드를 설정한다.
            if ((i & 0b00001) != 0) post.setYoutubeUrl("Test YouTube URL " + i);
            if ((i & 0b00010) != 0) post.setMapLocation("{\"latitude\": 37.5665, \"longitude\": 126.9780}");
            if ((i & 0b00100) != 0) post.setVotes("{\"upVotes\": 10, \"downVotes\": 5}");
            if ((i & 0b01000) != 0) post.setImageUrl("Test Image URL " + i);
            if ((i & 0b10000) != 0) post.setVideoUrl("Test Video URL " + i);

            // 평균 평점을 설정한다.
            post.setAverageRating(4.5);

            // 설정이 완료된 Post 객체를 데이터베이스에 저장한다.
            postRepository.save(post);
        }
    }
        @Test
        public void testGetAllPosts () throws Exception {
            mockMvc.perform(get("/api/posts"))
                    .andExpect(status().isOk());
        }

}
