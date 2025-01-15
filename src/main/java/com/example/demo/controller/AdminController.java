package com.example.demo.controller;

import com.example.demo.entity.PreferenceWeights;
import com.example.demo.entity.SystemConfig;
import com.example.demo.entity.User;
import com.example.demo.repository.SystemConfigRepository;
import com.example.demo.service.RecommendationService;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "ADMIN API", description = "관리자 관련 API 엔드포인트")
@RestController
@RequestMapping("/admin/preferences")
@RequiredArgsConstructor
public class AdminController {

    private final  RecommendationService recommendationService;

    private final  SystemConfigRepository systemConfigRepository;

    private final  UserService userService;
    @GetMapping
    @Operation(summary = "모든 사용자 조회")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/{userId}")
    @Operation(summary = "특정 ID의 사용자 조회")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 관리자가 선호도 가중치를 조회하는 엔드포인트
    @Operation(summary = "모든 선호도 가중치 조회")
    @GetMapping("/preferences")
    public ResponseEntity<List<PreferenceWeights>> getAllPreferences() {
        return ResponseEntity.ok(recommendationService.getAllPreferences());
    }

    // 관리자가 선호도 가중치를 추가하는 엔드포인트
    @Operation(summary = "새로운 선호도 가중치 추가")
    @PostMapping("/preferences")
    public ResponseEntity<PreferenceWeights> addPreference(@RequestBody PreferenceWeights preference) {
        return ResponseEntity.ok(recommendationService.addPreference(preference));
    }

    // 관리자가 선호도 가중치를 수정하는 엔드포인트
    @Operation(summary = "특정 ID의 선호도 가중치 수정")
    @PutMapping("/preferences/{id}")
    public ResponseEntity<PreferenceWeights> updatePreference(@PathVariable Long id, @RequestBody PreferenceWeights preference) {
        return ResponseEntity.ok(recommendationService.updatePreference(id, preference));
    }

    // 관리자가 선호도 가중치를 삭제하는 엔드포인트
    @Operation(summary = "특정 ID의 선호도 가중치 삭제")
    @DeleteMapping("/preferences/{id}")
    public ResponseEntity<Void> deletePreference(@PathVariable Long id) {
        recommendationService.deletePreference(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "최신 베이스 스코어 조회")
    @GetMapping("/basescore")
    public ResponseEntity<Double> getBaseScore() {
        double baseScore = recommendationService.getLatestBaseScore();
        return ResponseEntity.ok(baseScore);
    }
    @Operation(summary = "베이스 스코어 설정")
    @PostMapping("/basescore")
    public ResponseEntity<SystemConfig> setBaseScore(@RequestBody SystemConfig systemConfig) {
        SystemConfig updatedConfig = systemConfigRepository.save(systemConfig);
        return ResponseEntity.ok(updatedConfig);
    }


}