package com.example.demo.service;

import com.example.demo.entity.UserDestinationActivity;
import com.example.demo.repository.UserDestinationActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDestinationActivityService {

    @Autowired
    private UserDestinationActivityRepository activityRepository;

    public UserDestinationActivity saveActivity(UserDestinationActivity activity) {
        return activityRepository.save(activity);
    }

    public List<UserDestinationActivity> getAllActivities() {
        return activityRepository.findAll();
    }

    // 추가적인 서비스 메서드를 여기에 구현...
}
