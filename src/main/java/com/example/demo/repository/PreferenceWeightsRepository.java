package com.example.demo.repository;

import com.example.demo.entity.PreferenceWeights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferenceWeightsRepository extends JpaRepository<PreferenceWeights, Long> {

    List<PreferenceWeights> findByAgeRangeAndGender(String ageRange, String gender);

}
