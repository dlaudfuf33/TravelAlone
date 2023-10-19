package com.example.demo.service;

import com.example.demo.entity.PreferenceWeights;
import com.example.demo.repository.PreferenceWeightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PreferenceService {
    @Autowired
    private PreferenceWeightsRepository preferenceRepository;

    /**
     * 모든 선호도 가중치 목록을 반환합니다.
     * @return 선호도 가중치 목록
     */
    public List<PreferenceWeights> getAllPreferences() {
        return preferenceRepository.findAll();
    }

    /**
     * 주어진 ID에 해당하는 선호도 가중치를 반환합니다.
     * @param id 조회할 선호도 가중치의 ID
     * @return 선호도 가중치 (Optional)
     */
    public Optional<PreferenceWeights> getPreferenceById(Long id) {
        return preferenceRepository.findById(id);
    }

    /**
     * 새로운 선호도 가중치를 저장하거나 기존의 선호도 가중치를 업데이트합니다.
     * @param preference 저장 또는 업데이트할 선호도 가중치
     * @return 저장 또는 업데이트된 선호도 가중치
     */
    public PreferenceWeights savePreference(PreferenceWeights preference) {
        return preferenceRepository.save(preference);
    }

    /**
     * 주어진 ID의 선호도 가중치를 삭제합니다.
     * @param id 삭제할 선호도 가중치의 ID
     */
    public void deletePreference(Long id) {
        preferenceRepository.deleteById(id);
    }
}
