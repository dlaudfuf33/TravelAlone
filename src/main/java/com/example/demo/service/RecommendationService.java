package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private PreferenceWeightsRepository preferenceRepository;

    @Autowired
    private UserDestinationRepository userDestinationRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    /**
     * 사용자에게 추천 점수가 높은 여행지를 원하는 개수만큼 추천합니다.
     *
     * @param userId 사용자 ID (고유번호)
     * @param count  추천할 여행지의 개수
     * @return 추천 점수가 높은 여행지 리스트
     */
    public List<Destination> recommendDestinationsForUser(String userId, int count) {

        User user = userRepository.findByUserid(userId);
        Long userseq=user.getId();
        // 1. 사용자 프로필 생성 또는 가져오기
        Map<Destination, Double> userProfile = createUserProfile(userseq);
        // 사용자 프로필을 기반으로 추천 점수 계산
        Map<Destination, Double> recommendationScores = calculateRecommendationScores(userseq);

        // 사용자 프로필이 충분히 구축되지 않았거나 추천 점수가 낮은 경우 인기 있는 여행지를 기반으로 추천
        if (recommendationScores.isEmpty() || isUserProfileWeak(recommendationScores)) {
            return recommendPopularDestinations(count);
        }

        // 추천 점수가 높은 여행지 순으로 정렬
        List<Destination> sortedDestinations = recommendationScores.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 상위 N개의 여행지 반환
        return sortedDestinations.subList(0, Math.min(count, sortedDestinations.size()));
    }

    private boolean isUserProfileWeak(Map<Destination, Double> recommendationScores) {
        // 사용자 프로필의 강도를 판단하는 로직 (예: 추천 점수의 평균이 특정 임계값보다 낮은 경우)
        double averageScore = recommendationScores.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        return averageScore < getLatestBaseScore();  // 최신의 basescore 값을 사용
    }

    public double getLatestBaseScore() {
        return systemConfigRepository.findTopByOrderByModifiedDateDesc()
                .map(SystemConfig::getBasescore)
                .orElse(0.0);  // 기본값 설정. 적절한 값을 선택해야 합니다.
    }

    /**
     * 사용자 프로필을 생성하는 메서드.
     * 사용자가 평가한 여행지와 그에 대한 평점을 기반으로 사용자 프로필을 생성합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자 프로필 맵
     */
    public Map<Destination, Double> createUserProfile(Long userId) {
        List<UserDestination> userRatings = userDestinationRepository.findByUserId(userId);
        Map<Destination, Double> userProfile = new HashMap<>();

        for (UserDestination userRating : userRatings) {
            userProfile.put(userRating.getDestination(), userRating.getRating());
        }

        return userProfile;
    }

    /**
     * 사용자의 선호도 가중치를 적용하여 사용자 프로필의 점수를 수정합니다.
     *
     * @param userId     사용자 ID
     * @param userProfile 사용자 프로필 맵
     * @return 수정된 사용자 프로필 맵
     */
    private Map<Destination, Double> applyPreferenceWeights(Long userId, Map<Destination, Double> userProfile) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return userProfile;
        }

        String ageRange = user.getAgeRange();
        String gender = user.getGender();
        PreferenceWeights preferenceWeights = preferenceRepository.findByAgeRangeAndGender(ageRange, gender).get(0);
        double weight = preferenceWeights.getWeight();

        for (Map.Entry<Destination, Double> entry : userProfile.entrySet()) {
            double updatedScore = entry.getValue() * weight;
            userProfile.put(entry.getKey(), updatedScore);
        }

        return userProfile;
    }

    /**
     * 사용자 프로필과 여행지의 특성을 기반으로 추천 점수를 계산합니다.
     *
     * @param userId 사용자 ID
     * @return 추천 점수 맵
     */
    public Map<Destination, Double> calculateRecommendationScores(Long userId) {
        // 1. 사용자 프로필 생성
        Map<Destination, Double> userProfile = createUserProfile(userId);

        // 2. 선호도 가중치 적용
        userProfile = applyPreferenceWeights(userId, userProfile);

        Map<Destination, Double> recommendationScores = new HashMap<>();
        List<Destination> allDestinations = destinationRepository.findAll();

        // 3. 각 여행지에 대한 추천 점수 계산
        for (Destination destination : allDestinations) {
            double score = calculateScoreForDestination(userProfile, destination);
            recommendationScores.put(destination, score);
        }

        return recommendationScores;
    }

    /**
     * 여행지의 특성과 사용자 프로필을 비교하여 점수를 계산합니다.
     *
     * @param userProfile 사용자 프로필 맵
     * @param destination 여행지
     * @return 점수
     */
    private double calculateScoreForDestination(Map<Destination, Double> userProfile, Destination destination) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, List<String>> destinationFeatures = objectMapper.readValue(destination.getFeatures(), Map.class);

            double score = 0.0;
            List<String> features = destinationFeatures.get("특성");
            for (String feature : features) {
                if (userProfile.containsKey(destination) && userProfile.get(destination) >= 4.0) {
                    score += 5.0;
                }
            }

            return score;

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    /**
     * 인기 있는 여행지를 추천합니다.
     *
     * @param topN 추천 받고 싶은 여행지의 수
     * @return List<Destination> 추천 여행지 목록
     */
    public List<Destination> recommendPopularDestinations(int topN) {
        // 평균 평점이 높은 여행지를 순서대로 추출
        Pageable topNPageable = PageRequest.of(0, topN);
        // 평균 평점이 높은 상위 N개의 여행지를 조회하여 반환합니다.
        return destinationRepository.findTopNDestinationsByAverageRating(topNPageable);
    }

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
