package com.example.demo.controller;

import com.example.demo.entity.UserDestination;
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

    // 모든 사용자 여행지 목록을 가져오는 엔드포인트
    @GetMapping
    public ResponseEntity<List<UserDestination>> getAllUserDestinations() {
        return ResponseEntity.ok(userDestinationService.getAllUserDestinations());
    }

    // 특정 ID의 사용자 여행지를 가져오는 엔드포인트
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


    // 새로운 사용자 여행지를 생성하는 엔드포인트
    @PostMapping
    public ResponseEntity<UserDestination> createUserDestination(@RequestBody UserDestination userDestination) {
        return ResponseEntity.ok(userDestinationService.saveUserDestination(userDestination));
    }

    // 특정 ID의 사용자 여행지를 삭제하는 엔드포인트
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserDestination(@PathVariable Long id) {
        userDestinationService.deleteUserDestination(id);
        return ResponseEntity.noContent().build();
    }
}
