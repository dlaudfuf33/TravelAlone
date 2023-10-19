package com.example.demo.service;

import com.example.demo.entity.PreferenceWeights;
import com.example.demo.entity.Recommendation;
import com.example.demo.repository.PreferenceWeightsRepository;
import com.example.demo.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {
    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private PreferenceWeightsRepository preferenceRepository;

    // 모든 추천 목록을 가져오는 메서드
    public List<Recommendation> getAllRecommendations() {
        return recommendationRepository.findAll();
    }

    // 특정 ID의 추천을 가져오는 메서드
    public Optional<Recommendation> getRecommendationById(Long id) {
        return recommendationRepository.findById(id);
    }

    // 새로운 추천을 저장하는 메서드
    public Recommendation saveRecommendation(Recommendation recommendation) {
        return recommendationRepository.save(recommendation);
    }

    // 특정 ID의 추천을 삭제하는 메서드
    public void deleteRecommendation(Long id) {
        recommendationRepository.deleteById(id);
    }

    // 관리자가 선호도 가중치를 조회하는 메서드
    public List<PreferenceWeights> getAllPreferences() {
        return preferenceRepository.findAll();
    }

    // 관리자가 선호도 가중치를 추가하는 메서드
    public PreferenceWeights addPreference(PreferenceWeights preference) {
        return preferenceRepository.save(preference);
    }

    // 관리자가 선호도 가중치를 수정하는 메서드
    public PreferenceWeights updatePreference(Long id, PreferenceWeights preference) {
        if (preferenceRepository.existsById(id)) {
            preference.setId(id);
            return preferenceRepository.save(preference);
        } else {
            return null; // 해당 ID의 선호도 가중치가 없을 경우 null 반환
        }
    }

    // 관리자가 선호도 가중치를 삭제하는 메서드
    public void deletePreference(Long id) {
        preferenceRepository.deleteById(id);
    }
}
