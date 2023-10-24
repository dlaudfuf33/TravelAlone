package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private PreferenceWeightsRepository preferenceRepository;

    @Autowired
    private UserDestinationRepository userDestinationRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private UserDestinationActivityRepository userDestinationActivityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SystemConfigRepository systemConfigRepository;


    public List<Destination> recommendDestinationsForUser(String userId, int count) {
        logger.info("사용자 {}에 대한 추천 프로세스 시작", userId);

        // 1. 사용자 정보 조회
        User user = userRepository.findByUserid(userId);
        logger.info("사용자 정보 조회 완료: {}", user);

        // 2. 사용자 프로필 생성
        Map<Destination, Double> userProfile = createUserProfile(user.getId());
        logger.info("사용자 프로필 생성 완료: {}", userProfile);

        // 3. 각 여행지에 대한 추천 점수 계산
        Map<Destination, Double> recommendationScores = calculateRecommendationScores(user.getId());
        logger.info("추천 점수 계산 완료: {}", recommendationScores);

        // 4. 사용자의 행동 데이터를 기반으로 가중치 계산
        double activityWeight = calculateUserActivityWeight(user.getId());
        logger.info("사용자 활동 가중치 계산 완료: {}", activityWeight);

        // 5. 기존의 추천 점수에 가중치 추가
        for (Map.Entry<Destination, Double> entry : recommendationScores.entrySet()) {
            double updatedScore = entry.getValue() + activityWeight;
            recommendationScores.put(entry.getKey(), updatedScore);
        }
        logger.info("활동 가중치를 사용하여 추천 점수 업데이트 완료");

        // 6. 사용자가 평가한 여행지의 점수를 확인하고 임계값 이상인 경우 해당 여행지의 특성 추출
        double baseScore = getLatestBaseScore();
        logger.info("최신 기준 점수 조회 완료: {}", baseScore);

        List<Destination> destinationsWithSimilarFeaturesByRating = new ArrayList<>();
        for (Map.Entry<Destination, Double> entry : userProfile.entrySet()) {
            if (entry.getValue() >= baseScore) {
                List<String> features = extractFeaturesFromDestination(entry.getKey().getId());
                destinationsWithSimilarFeaturesByRating.addAll(findDestinationsByFeatures(features));
            }
        }
        logger.info("평가 기반 유사 특성 여행지 조회 완료: {}", destinationsWithSimilarFeaturesByRating);

        // 7. 사용자의 행동 점수를 기반으로 특성과 유사한 여행지 추천
        if (activityWeight >= baseScore) {
            for (Map.Entry<Destination, Double> entry : recommendationScores.entrySet()) {
                if (entry.getValue() >= baseScore) {
                    List<String> features = extractFeaturesFromDestination(entry.getKey().getId());
                    destinationsWithSimilarFeaturesByRating.addAll(findDestinationsByFeatures(features));
                }
            }
        }
        logger.info("행동 점수 기반 유사 특성 여행지 조회 완료");

        // 8. 사용자 프로필의 강도 판단
        if (isUserProfileWeak(recommendationScores)) {
            // 사용자 프로필이 약한 경우 인기 여행지를 기반으로 추천
            logger.info("사용자 프로필이 약함. 인기 여행지 기반 추천 시작");
            return recommendPopularDestinations(count);
        } else {
            // 9. 중복 제거 및 상위 N개의 여행지 반환
            logger.info("중복 제거 및 상위 {}개의 여행지 반환 시작", count);
            return destinationsWithSimilarFeaturesByRating.stream()
                    .distinct()
                    .limit(count)
                    .collect(Collectors.toList());
        }
    }



    private boolean isUserProfileWeak(Map<Destination, Double> recommendationScores) {
        // 사용자 프로필의 강도를 판단하는 로직 (예: 추천 점수의 평균이 특정 임계값보다 낮은 경우)
        double averageScore = recommendationScores.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        return averageScore < getLatestBaseScore();  // 최신의 basescore 값을 사용
    }

    public double getLatestBaseScore() {
        return systemConfigRepository.findTopByOrderByModifiedDateDesc()
                .map(SystemConfig::getBasescore)
                .orElse(2.0);  // 기본값 설정. 적절한 값을 선택해야 합니다.
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

        List<PreferenceWeights> preferences = preferenceRepository.findByAgeRangeAndGender(ageRange, gender);

        if (preferences == null || preferences.isEmpty()) {
            // 적절한 처리 또는 기본값 설정
            return userProfile;
        }

        PreferenceWeights preferenceWeights = preferences.get(0);

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
        // 사용자가 평가한 여행지와 그에 대한 평점을 기반으로 사용자 프로필을 생성합니다.

        Map<Destination, Double> userProfile = createUserProfile(userId);

        // 2. 선호도 가중치 적용
        // 사용자의 연령대와 성별에 따른 선호도 가중치를 적용하여 사용자 프로필의 점수를 수정합니다.

        userProfile = applyPreferenceWeights(userId, userProfile);

        Map<Destination, Double> recommendationScores = new HashMap<>();
        List<Destination> allDestinations = destinationRepository.findAll();

        // 3. 각 여행지에 대한 추천 점수 계산
        // 여행지의 특성과 사용자 프로필을 비교하여 각 여행지에 대한 점수를 계산합니다.
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
            List<String> features = objectMapper.readValue(destination.getFeatures(), List.class); // 직접 List로 변환

            double score = 0.0;
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


    /**
     * 사용자의 행동 데이터를 기반으로 가중치를 계산하는 메서드.
     *
     * @param userId 사용자 ID
     * @return 계산된 가중치
     */
    public double calculateUserActivityWeight(Long userId) {
        // 사용자의 행동 데이터를 조회합니다.
        List<UserDestinationActivity> activities = userDestinationActivityRepository.findByUserId(userId);
        double totalWeight = 0.0;

        for (UserDestinationActivity activity : activities) {
            double weight = 0.0;

            // 머문 시간 계산
            long duration = activity.getEndTime() - activity.getStartTime();

            // 5초 이상 머물렀을 경우 가중치 추가
            if (duration >= 5000) {
                weight += 5.0;
            }

            // 사진 클릭 및 스크롤에 따른 가중치 추가
            if (activity.getPhotoClicked()) {
                weight += 10.0;
            }
            if (activity.getScrolled()) {
                weight += 5.0;
            }

            totalWeight += weight;
        }

        // 모든 활동 데이터의 가중치 평균을 반환
        return activities.isEmpty() ? 0.0 : totalWeight / activities.size();
    }

    /**
     * 특정 여행지의 특성을 JSON 형태에서 List<String>으로 변환하여 반환합니다.
     *
     * @param destinationId 특성을 추출하려는 여행지의 ID
     * @return 해당 여행지의 특성 목록
     */
    public List<String> extractFeaturesFromDestination(Long destinationId) {
        // 1. 여행지 ID를 사용하여 해당 여행지 정보를 조회합니다.
        Destination destination = destinationRepository.findDestinationById(destinationId);

        // 2. 여행지 정보에서 특성을 JSON 형태로 가져옵니다.
        String featuresJson = destination.getFeatures();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 3. JSON 형태의 특성을 List<String>으로 변환합니다.
            return objectMapper.readValue(featuresJson, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            // 4. 변환 중 오류가 발생하면 오류를 출력하고 빈 목록을 반환합니다.
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    /**
     * 주어진 특성 목록과 일치하는 여행지를 검색하여 반환합니다.
     *
     * @param features 검색하려는 특성 목록
     * @return 특성과 일치하는 여행지 목록
     */
    public List<Destination> findDestinationsByFeatures(List<String> features) {
        // 1. 모든 여행지 정보를 조회합니다.
        return destinationRepository.findAll().stream()
                .filter(destination -> {
                    // 2. 각 여행지의 특성과 주어진 특성 목록을 비교합니다.
                    for (String feature : features) {
                        if (!destination.getFeatures().contains(feature)) {
                            return false;
                        }
                    }
                    return true;
                })
                // 3. 일치하는 여행지를 목록으로 반환합니다.
                .collect(Collectors.toList());
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
