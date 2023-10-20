package com.example.demo;

import com.example.demo.entity.PreferenceWeights;
import com.example.demo.repository.PreferenceWeightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class PreferenceInitializer {

    @Autowired
    private PreferenceWeightsRepository preferenceWeightsRepository;

    // 애플리케이션 시작 시에 자동으로 실행되는 메서드
    @PostConstruct
    public void init() {
        // 나이대, 성별, 계절, 지역, 여행 유형에 따른 모든 경우의 수를 고려하여 기본 가중치 설정
        String[] ageRanges = {"10", "20", "30", "40", "50", "60이상"};
        String[] genders = {"남", "녀"};
        String[] seasons = {"봄", "여름", "가을", "겨울"};
        String[] locations = {"서울", "부산", "제주", "강원"};
        String[] travelTypes = {"가족여행", "커플여행", "친구와의 여행", "혼자 여행"};

        List<PreferenceWeights> preferences = new ArrayList<>();

        for (String ageRange : ageRanges) {
            for (String gender : genders) {
                for (String season : seasons) {
                    for (String location : locations) {
                        for (String travelType : travelTypes) {
                            PreferenceWeights preference = new PreferenceWeights();
                            preference.setAgeRange(ageRange);
                            preference.setGender(gender);
                            preference.setSeason(season);
                            preference.setLocation(location);
                            preference.setTravelType(travelType);
                            // 기본 가중치 설정 (추후 조정 가능)
                            preference.setWeight(1.0);
                            preferences.add(preference);
                        }
                    }
                }
            }
        }

        // 데이터베이스에 초기 가중치 저장
        preferenceWeightsRepository.saveAll(preferences);
    }
}