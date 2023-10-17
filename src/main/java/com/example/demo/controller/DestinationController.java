package com.example.demo.controller;

import com.example.demo.entity.Destination;
import com.example.demo.service.DestinationService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/destinations")
public class DestinationController {
    @Autowired
    private DestinationService destinationService;

    @Autowired
    private UserService userService;

    // 모든 여행지 목록을 가져오는 엔드포인트
    @GetMapping
    public ResponseEntity<List<Destination>> getAllDestinations() {
        // 1. 여행지 서비스를 통해 모든 여행지 목록을 조회합니다.
        List<Destination> destinations = destinationService.getAllDestinations();

        // 2. 조회된 여행지 목록을 클라이언트에게 성공 응답(200 OK)으로 반환합니다.
        return ResponseEntity.ok(destinations);
    }

    // 특정 ID의 여행지를 가져오는 엔드포인트
    @GetMapping("/{id}")
    public ResponseEntity<Destination> getDestinationById(@PathVariable Long id) {
        // 1. 여행지 서비스를 통해 ID에 해당하는 여행지 정보를 조회합니다.
        Optional<Destination> destinationOptional = destinationService.getDestinationById(id);
        // 2. 조회된 여행지 정보가 있으면 해당 정보를 클라이언트에게
        // 성공 응답(200 OK)으로 반환합니다.
        if (destinationOptional.isPresent()) {
            return ResponseEntity.ok(destinationOptional.get());
        }
        // 3. 조회된 여행지 정보가 없으면 404 Not Found 상태 코드를 클라이언트에게 반환합니다.
        else {
            return ResponseEntity.notFound().build();
        }
    }

    // 새로운 여행지를 생성하는 엔드포인트
    @PostMapping
    public ResponseEntity<Destination> createDestination(@RequestBody Destination destination) {
        // 1. 여행지 서비스를 통해 새로운 여행지를 생성하고 저장합니다.
        Destination createdDestination = destinationService.saveDestination(destination);

        // 2. 생성된 여행지 정보를 클라이언트에게 성공 응답(200 OK)으로 반환합니다.
        return ResponseEntity.ok(createdDestination);
    }

    // 특정 ID의 여행지를 삭제하는 엔드포인트
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id) {
        // 1. 여행지 서비스를 통해 ID에 해당하는 여행지를 삭제합니다.
        destinationService.deleteDestination(id);

        // 2. 삭제가 성공적으로 수행되면 클라이언트에게 성공 응답(204 No Content)을 반환합니다.
        return ResponseEntity.noContent().build();
    }

    // 인기 있는 여행지를 추천하는 엔드포인트
    @GetMapping("/recommend/popular")
    public List<Destination> getPopularDestinations(@RequestParam int topN) {
        // 1. 사용자 서비스를 통해 인기 있는 여행지를 추천받습니다.
        List<Destination> recommendedDestinations =
                userService.recommendPopularDestinations(topN);

        // 2. 추천된 여행지 목록을 클라이언트에게 반환합니다.
        return recommendedDestinations;
    }
}
