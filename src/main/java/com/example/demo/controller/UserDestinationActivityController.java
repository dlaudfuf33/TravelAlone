package com.example.demo.controller;

import com.example.demo.entity.Destination;
import com.example.demo.entity.User;
import com.example.demo.entity.UserDestinationActivity;
import com.example.demo.exception.InvalidTokenException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.repository.DestinationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TokenService;
import com.example.demo.service.UserDestinationActivityService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class UserDestinationActivityController {


    private final UserDestinationActivityService activityService;

    private final TokenService tokenService;

    private final DestinationRepository destinationRepository;


    // 사용자의 활동을 기록하는 엔드포인트
    @PostMapping("/collect")
    public void collectActivity(@RequestHeader("Authorization") String token, @RequestBody UserDestinationActivity activityData) {
        String jwtToken = token.substring(7);
        try {
            Long userid = tokenService.getUserIdFromToken(jwtToken);
            activityService.saveActivity(userid, activityData);
        } catch (InvalidTokenException e) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }

        Claims claims = tokenService.parseToken(jwtToken);
        if (claims != null) {
            String userid = (String) claims.get("sub");
            activityService.saveActivity(Long.valueOf(userid), activityData);
        }
    }

    // 모든 사용자의 활동을 조회하는 엔드포인트
    @GetMapping
    public List<UserDestinationActivity> getAllActivities() {
        return activityService.getAllActivities();
    }

    // 특정 ID를 가진 사용자의 활동을 조회하는 엔드포인트
    @GetMapping("/{id}")
    public ResponseEntity<UserDestinationActivity> getActivityById(@PathVariable Long id) {
        UserDestinationActivity activity = activityService.getActivityById(id);
        if (activity != null) {
            return ResponseEntity.ok(activity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 특정 ID를 가진 사용자의 활동을 업데이트하는 엔드포인트
    @PutMapping("/{id}")
    public ResponseEntity<UserDestinationActivity> updateActivity(@PathVariable Long id, @RequestBody UserDestinationActivity updatedActivity) {
        UserDestinationActivity activity = activityService.updateActivity(id, updatedActivity);
        if (activity != null) {
            return ResponseEntity.ok(activity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 특정 ID를 가진 사용자의 활동을 삭제하는 엔드포인트
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteActivity(@PathVariable Long id) {
        boolean deleted = activityService.deleteActivity(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
