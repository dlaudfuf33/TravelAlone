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

}