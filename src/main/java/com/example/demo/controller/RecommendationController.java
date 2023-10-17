package com.example.demo.controller;

import com.example.demo.entity.Recommendation;
import com.example.demo.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    // 모든 추천 목록을 가져오는 엔드포인트
    @GetMapping
    public ResponseEntity<List<Recommendation>> getAllRecommendations() {
        return ResponseEntity.ok(recommendationService.getAllRecommendations());
    }

    // 특정 ID의 추천을 가져오는 엔드포인트
    @GetMapping("/{id}")
    public ResponseEntity<Recommendation> getRecommendationById(@PathVariable Long id) {
        // 1. 서비스를 통해 ID에 해당하는 추천 정보를 조회합니다.
        Optional<Recommendation> recommendationOptional = recommendationService.getRecommendationById(id);

        // 2. 조회된 추천 정보가 있으면 해당 정보를 반환합니다.
        if (recommendationOptional.isPresent()) {
            return ResponseEntity.ok(recommendationOptional.get());
        }
        // 3. 조회된 추천 정보가 없으면 404 Not Found 상태 코드를 반환합니다.
        else {
            return ResponseEntity.notFound().build();
        }
    }



    // 새로운 추천을 생성하는 엔드포인트
    @PostMapping
    public ResponseEntity<Recommendation> createRecommendation(@RequestBody Recommendation recommendation) {
        return ResponseEntity.ok(recommendationService.saveRecommendation(recommendation));
    }

    // 특정 ID의 추천을 삭제하는 엔드포인트
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Long id) {
        recommendationService.deleteRecommendation(id);
        return ResponseEntity.noContent().build();
    }
}
