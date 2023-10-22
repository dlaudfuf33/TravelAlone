package com.example.demo.controller;

import com.example.demo.entity.Destination;
import com.example.demo.entity.User;
import com.example.demo.entity.UserDestination;
import com.example.demo.repository.DestinationRepository;
import com.example.demo.repository.UserDestinationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.DestinationService;
import com.example.demo.service.UserDestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/userDestinations")
public class UserDestinationController {

    @Autowired
    private UserDestinationService userDestinationService;
    @Autowired
    private DestinationService destinationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DestinationRepository destinationRepository;

    // 모든 사용자 여행지 리뷰 목록을 가져오는 엔드포인트
    @GetMapping
    public ResponseEntity<List<UserDestination>> getAllUserDestinations() {
        return ResponseEntity.ok(userDestinationService.getAllUserDestinations());
    }

    // 특정 ID의 사용자 여행지 리뷰를 가져오는 엔드포인트
    @GetMapping("/{id}")
    public ResponseEntity<UserDestination> getUserDestinationById(@PathVariable Long id) {
        Optional<UserDestination> userDestinationOptional =
                userDestinationService.getUserDestinationById(id);
        if (userDestinationOptional.isPresent()) {
            return ResponseEntity.ok(userDestinationOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/review")
    public ResponseEntity<UserDestination> leaveReview(
            @RequestParam String userId,
            @RequestParam Long destinationId,
            @RequestParam Double rating,
            @RequestParam String review) {

        User user = userRepository.findByUserid(userId);
        Destination destination = destinationRepository.findDestinationById(destinationId);
        // 리뷰와 평점을 저장하기 위한 UserDestination 객체 생성
        UserDestination userDestination = new UserDestination();
        userDestination.setUser(user);
        userDestination.setDestination(destination);
        userDestination.setRating(rating);
        userDestination.setReview(review);

        // 리뷰와 평점 저장
        UserDestination savedUserDestination = userDestinationService.saveUserDestination(userDestination);

        // 여행지의 평균 평점 업데이트 (이 부분은 DestinationService에 구현되어 있어야 합니다.)
        destinationService.updateAverageRatingForDestination(destinationId);

        return ResponseEntity.ok(savedUserDestination);
    }


    // 특정 ID의 사용자 여행지리뷰를 삭제하는 엔드포인트
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserDestination(@PathVariable Long id) {
        userDestinationService.deleteUserDestination(id);
        return ResponseEntity.noContent().build();
    }
}
