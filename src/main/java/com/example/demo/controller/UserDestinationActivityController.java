package com.example.demo.controller;

import com.example.demo.entity.Destination;
import com.example.demo.entity.User;
import com.example.demo.entity.UserDestinationActivity;
import com.example.demo.repository.DestinationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TokenService;
import com.example.demo.service.UserDestinationActivityService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class UserDestinationActivityController {

    @Autowired
    private UserDestinationActivityService activityService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DestinationRepository destinationRepository;



    // 사용자의 활동을 생성하는 엔드포인트
    @PostMapping("/collect")
    public ResponseEntity<?> collectActivity(@RequestHeader("Authorization") String token,@RequestBody UserDestinationActivity activityData) {
        System.out.println(activityData+"#######");
        String jwtToken = token.substring(7); // "Bearer " 부분을 제거

        Claims claims=tokenService.parseToken(jwtToken);
        if(claims!=null) {
            String userid = (String) claims.get("sub");

            User user = userRepository.findByUserid(userid);
            // 세션에서 가져온 사용자 정보를 activityData에 설정
            Destination destination = destinationRepository.findById(activityData.getDestination().getId()).orElse(null);
            if (destination == null) {
                return ResponseEntity.badRequest().body("해당 여행지를 찾을 수 없습니다.");
            }
            activityData.setUser(user);
            activityData.setDestination(destination);
        }else{
            System.out.println("비회원");
        }

        // 활동 데이터 저장
        UserDestinationActivity savedActivity = activityService.saveActivity(activityData);

        return ResponseEntity.ok(savedActivity);
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
