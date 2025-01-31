package com.example.demo.controller;

import com.example.demo.entity.Destination;
import com.example.demo.entity.Recommendation;
import com.example.demo.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "RECOMMENDATIONS API", description = "추천(Recommendations) 관련 API 엔드포인트")
@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);


    private final  RecommendationService recommendationService;

    /**
     * 모든 추천 목록을 가져오는 엔드포인트
     * @return 모든 추천 목록
     */
    @Operation(summary = "모든 추천 목록 조회")
    @GetMapping
    public ResponseEntity<List<Recommendation>> getAllRecommendations() {
        logger.info("1. 서비스를 통해 모든 추천 목록을 조회합니다.");
        // 1. 서비스를 통해 모든 추천 목록을 조회합니다.
        return ResponseEntity.ok(recommendationService.getAllRecommendations());
    }

    /**
     * 특정 ID의 추천을 가져오는 엔드포인트
     * @param id 추천 ID
     * @return 해당 ID의 추천 정보
     */
    @Operation(summary = "특정 ID의 추천 조회")
    @GetMapping("/{id}")
    public ResponseEntity<Recommendation> getRecommendationById(@PathVariable Long id) {
        // 1. 서비스를 통해 ID에 해당하는 추천 정보를 조회합니다.
        Optional<Recommendation> recommendationOptional = recommendationService.getRecommendationById(id);
        // 2. 조회된 추천 정보를 반환합니다.
        return recommendationOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 특정 ID의 추천을 삭제하는 엔드포인트
     * @param id 삭제할 추천 ID
     * @return 삭제 성공 시 204 No Content 상태 코드
     */
    @Operation(summary = "특정 ID의 추천 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Long id) {
        // 1. 서비스를 통해 ID에 해당하는 추천 정보를 삭제합니다.
        recommendationService.deleteRecommendation(id);
        // 2. 삭제 성공 시 204 No Content 상태 코드를 반환합니다.
        return ResponseEntity.noContent().build();
    }

    /**
     * 특정 사용자에게 여행지를 추천하는 엔드포인트
     * @param userId 사용자 ID
     * @param count 추천 받고 싶은 여행지의 수
     * @return 추천된 여행지 목록
     */
    @Operation(summary = "사용자에게 여행지 개인추천")
    @GetMapping("/foruser/{userId}/{count}")
    public ResponseEntity<List<Destination>> recommendDestinationsForUser(
            @PathVariable String userId,
            @PathVariable int count
    ) {
        // userId와 count를 사용하여 추천 여행지를 가져옴
        List<Destination> recommendedDestinations = recommendationService.recommendDestinationsForUser(userId, count);
        // 추천된 여행지를 ResponseEntity로 래핑하여 반환
        return ResponseEntity.ok(recommendedDestinations);
    }
}
