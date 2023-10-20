package com.example.demo.controller;

import com.example.demo.entity.UserDestinationActivity;
import com.example.demo.service.UserDestinationActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class UserDestinationActivityController {

    @Autowired
    private UserDestinationActivityService activityService;

    @PostMapping
    public UserDestinationActivity createActivity(@RequestBody UserDestinationActivity activity) {
        return activityService.saveActivity(activity);
    }

    @GetMapping
    public List<UserDestinationActivity> getAllActivities() {
        return activityService.getAllActivities();
    }

    // 추가적인 엔드포인트를 여기에 구현...
}
