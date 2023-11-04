//package com.example.demo;
//
//import com.example.demo.entity.Destination;
//import com.example.demo.repository.DestinationRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class DestinationControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private DestinationRepository destinationRepository;
//
//    @BeforeEach
//    public void setUp() {
//        destinationRepository.deleteAll();
//
//        List<Destination> destinations = new ArrayList<>();
//
//        destinations.add(new Destination("제주도", "한국의 아름다운 섬", 4.5, "https://forlivealone.s3.ap-northeast-2.amazonaws.com/1697450804244_ghang-an.jpeg", "{\"특성\": [\"해변\", \"오름\", \"한라산\", \"돌하르방\"]}"));
//        destinations.add(new Destination("부산", "해운대와 광안리의 아름다운 해변", 4.3, "https://forlivealone.s3.ap-northeast-2.amazonaws.com/1697450804244_ghang-an.jpeg", "{\"특성\": [\"해변\", \"국제시장\", \"부산타워\", \"야경\"]}"));
//        destinations.add(new Destination("서울", "한국의 수도", 4.7, "https://forlivealone.s3.ap-northeast-2.amazonaws.com/1697450828762_namsan.jpeg", "{\"특성\": [\"전통시장\", \"궁전\", \"타워\", \"강\"]}"));
//        destinations.add(new Destination("경주", "한국의 역사적인 도시", 4.6, "https://forlivealone.s3.ap-northeast-2.amazonaws.com/1697450883278_gyung-ju.jpeg", "{\"특성\": [\"불국사\", \"첨성대\", \"계림\", \"역사적 유적지\"]}"));
//        destinations.add(new Destination("강원도", "겨울의 왕국, 스키와 눈썰매의 천국", 4.4, "https://forlivealone.s3.ap-northeast-2.amazonaws.com/1697450867863_de-ghan.jpeg", "{\"특성\": [\"스키장\", \"산\", \"계곡\", \"바다\"]}"));
//
//        destinationRepository.saveAll(destinations);
//    }
//
//    @Test
//    public void testGetAllDestinations() throws Exception {
//        mockMvc.perform(get("/api/destinations"))
//                .andExpect(status().isOk());
//    }
//}
