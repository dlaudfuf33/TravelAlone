package com.example.demo.controller;

import com.example.demo.entity.PreferenceWeights;
import com.example.demo.service.PreferenceService;
import com.example.demo.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/admin/preferences")
public class AdminController {
    @Autowired
    private RecommendationService recommendationService;

    // 관리자가 선호도 가중치를 조회하는 엔드포인트
    @GetMapping("/preferences")
    public ResponseEntity<List<PreferenceWeights>> getAllPreferences() {
        return ResponseEntity.ok(recommendationService.getAllPreferences());
    }

    // 관리자가 선호도 가중치를 추가하는 엔드포인트
    @PostMapping("/preferences")
    public ResponseEntity<PreferenceWeights> addPreference(@RequestBody PreferenceWeights preference) {
        return ResponseEntity.ok(recommendationService.addPreference(preference));
    }

    // 관리자가 선호도 가중치를 수정하는 엔드포인트
    @PutMapping("/preferences/{id}")
    public ResponseEntity<PreferenceWeights> updatePreference(@PathVariable Long id, @RequestBody PreferenceWeights preference) {
        return ResponseEntity.ok(recommendationService.updatePreference(id, preference));
    }

    // 관리자가 선호도 가중치를 삭제하는 엔드포인트
    @DeleteMapping("/preferences/{id}")
    public ResponseEntity<Void> deletePreference(@PathVariable Long id) {
        recommendationService.deletePreference(id);
        return ResponseEntity.noContent().build();
    }
}