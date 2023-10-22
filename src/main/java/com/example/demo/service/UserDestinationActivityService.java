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

    // ID를 기반으로 활동을 조회하는 메서드
    public UserDestinationActivity getActivityById(Long id) {
        return activityRepository.findById(id).orElse(null);
    }

    // ID를 기반으로 활동을 업데이트하는 메서드
    public UserDestinationActivity updateActivity(Long id, UserDestinationActivity updatedActivity) {
        if (activityRepository.existsById(id)) {
            updatedActivity.setId(id); // ID를 설정하여 기존 항목을 대체합니다.
            return activityRepository.save(updatedActivity);
        } else {
            return null; // 해당 ID의 활동이 없는 경우 null을 반환합니다.
        }
    }

    // ID를 기반으로 활동을 삭제하는 메서드
    public boolean deleteActivity(Long id) {
        if (activityRepository.existsById(id)) {
            activityRepository.deleteById(id);
            return true; // 삭제 성공
        } else {
            return false; // 해당 ID의 활동이 없는 경우 false를 반환합니다.
        }

    }
}
