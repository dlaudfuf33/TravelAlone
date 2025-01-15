package com.example.demo.controller;

import com.example.demo.MediaProcessor;
import com.example.demo.entity.CreateDestinationRequest;
import com.example.demo.entity.Destination;
import com.example.demo.entity.UpdateDestinationRequest;
import com.example.demo.service.DestinationService;
import com.example.demo.service.RecommendationService;
import com.example.demo.service.TokenService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "DESTINATIONS API", description = "여행지(Destinations) 관련 API 엔드포인트")
@RestController
@RequestMapping("/api/destinations")
@RequiredArgsConstructor
public class DestinationController {

    private final  DestinationService destinationService;


    private final  RecommendationService recommendationService;


    private final  MediaProcessor mediaProcessor;


    private final  TokenService tokenService;

    // 모든 여행지 목록을 가져오는 엔드포인트
    @Operation(summary = "모든 여행지 목록 조회")
    @GetMapping
    public ResponseEntity<List<Destination>> getAllDestinations() {
        // 1. 여행지 서비스를 통해 모든 여행지 목록을 조회합니다.
        List<Destination> destinations = destinationService.getAllDestinations();

        // 2. 조회된 여행지 목록을 클라이언트에게 성공 응답(200 OK)으로 반환합니다.
        return ResponseEntity.ok(destinations);
    }

    // 특정 ID의 여행지를 가져오는 엔드포인트
    @Operation(summary = "특정 ID의 여행지 조회")
    @GetMapping("/view/{id}")
    public ResponseEntity<Destination> getDestinationById(@PathVariable Long id) {
        // 1. 여행지 서비스를 통해 ID에 해당하는 여행지 정보를 조회합니다.
        System.out.println("1__ : "+id);
        Optional<Destination> destinationOptional = destinationService.getDestinationById(id);
        System.out.println("2__ : "+destinationOptional);
        // 2. 조회된 여행지 정보가 있으면 해당 정보를 클라이언트에게
        // 성공 응답(200 OK)으로 반환합니다.
        if (destinationOptional.isPresent()) {
            return ResponseEntity.ok(destinationOptional.get());
        }
        // 3. 조회된 여행지 정보가 없으면 404 Not Found 상태 코드를 클라이언트에게 반환합니다.
        else {
            System.out.println("Fail__ : "+destinationOptional);

            return ResponseEntity.notFound().build();
        }
    }
    // 작성자명으로 여행지를 가져오는 엔드포인트
    @Operation(summary = "특정 작성자명으로 여행지 조회")
    @GetMapping("/searchByAuthor/{authorName}")
    public ResponseEntity<List<Destination>> searchByAuthor(@PathVariable String authorName) {
        List<Destination> destinations = destinationService.searchByAuthor(authorName);

        if (!destinations.isEmpty()) {
            return ResponseEntity.ok(destinations);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "새로운 여행지 생성")
    @PostMapping("/create")
    public ResponseEntity<?> createDestination(@RequestHeader("Authorization") String token, @RequestBody CreateDestinationRequest request) {
        try {
            String userId="";
            if (token!=null) {
                // 토큰 검증
                String jwtToken = token.substring(7); // "Bearer " 제거
                if (tokenService.isTokenBlacklisted(jwtToken)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 블랙리스트에 있습니다.");
                }
                Claims claims = tokenService.parseToken(jwtToken);
                if (claims == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 블랙리스트에 있습니다.");
                }
                userId = (String) claims.get("sub");
            }
            Destination newDestination = new Destination();

            // 작성자명과 비밀번호로 회원 여부 확인
            if (request.getAuthor() != null && request.getPassword() != null) {
                // 비회원인 경우
                newDestination.setUserId(""); // 또는 원하는 값으로 설정
            } else if (userId != null) {
                // 회원인 경우
                String userIdFromRequest = userId;
                newDestination.setAuthor(userIdFromRequest);
                newDestination.setUserId(userIdFromRequest);
                newDestination.setPassword(null);
            } else {
                return ResponseEntity.badRequest().body("작성자명 또는 비밀번호가 누락되었습니다.");
            }
            // 1. 사용자 입력에서 여행지 정보를 추출합니다.
            String name = request.getName();
            String region = request.getRegion();
            String contents = request.getContents();
            Double userRating = request.getUserRating();
            String recommendedSeason = request.getRecommendedSeason();

            String featuresString = request.getFeaturesString();
            if (featuresString != null) {
                List<String> features = Arrays.stream(featuresString.split("#"))
                        .filter(tag -> !tag.isEmpty()) // 빈 문자열 제외
                        .map(tag -> "#" + tag) // 각 태그 앞에 # 추가
                        .collect(Collectors.toList());
                newDestination.setFeatures(features);
            }

            // 2. 새로운 여행지 엔터티를 생성합니다.
            newDestination.setName(name);
            newDestination.setRegion(region);

            // 사용자가 입력한 평점을 추가합니다.
            newDestination.addRating(userRating);

            // 추천 계절을 설정합니다.
            // 추천 계절을 리스트로 변환합니다.
            if (recommendedSeason != null) {
                List<String> bestSeasons = Arrays.asList(recommendedSeason.split(","));
                newDestination.setBestSeasons(bestSeasons); // 변경된 필드명에 맞게 메서드 이름을 수정하세요.
            }

            // 3. contents를 파싱하여 이미지와 비디오 데이터를 추출하고 S3에 업로드합니다.
            Document document = Jsoup.parse(contents); // HTML 파싱
            mediaProcessor.processMediaDestination(document, newDestination);
            String updatedContent = document.outerHtml();
            newDestination.setContents(updatedContent);
            System.out.println(updatedContent);

            // 4. 대표 이미지 설정
            String imageUrl = mediaProcessor.extractImageUrl(updatedContent);
            if (imageUrl != null) {
                newDestination.setImageUrl(imageUrl);
            }

            // 5. 여행지 서비스를 통해 새로운 여행지를 저장합니다.
            Destination createdDestination = destinationService.saveDestination(newDestination);

            // 6. 생성된 여행지 정보를 클라이언트에게 성공 응답(200 OK)으로 반환합니다.
            return ResponseEntity.ok(createdDestination);
        } catch (Exception e) {
            // 6. 생성 중 오류가 발생한 경우 예외를 처리합니다.
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "여행지 정보 업데이트")
    @PostMapping("/update/{destinationId}")
    public ResponseEntity<?> updateDestination(
            @PathVariable("destinationId") Long destinationId,
            @RequestBody UpdateDestinationRequest request
    ) {
        try {
            System.out.println(destinationId);System.out.println(request);
            // 1. 업데이트하려는 여행지 정보를 조회합니다.
            Optional<Destination> optionalDestination = destinationService.getDestinationById(destinationId);
            if (!optionalDestination.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Destination destination = optionalDestination.get();

            // 2. 업데이트할 필드를 선택하고 해당 필드를 업데이트합니다.
            Map<String, String> updateFields = request.getUpdateFields();
            for (Map.Entry<String, String> entry : updateFields.entrySet()) {
                String updateField = entry.getKey();
                String newValue = entry.getValue();
                switch (updateField) {
                    case "name":
                        destination.setName(newValue);
                        break;
                    case "region":
                        destination.setRegion(newValue);
                        break;
                    case "contents":
                        // contents 업데이트
                        Document document = Jsoup.parse(newValue); // HTML 파싱
                        mediaProcessor.processMediaDestination(document, destination);
                        String updatedContent = document.outerHtml();
                        System.out.println(updatedContent+"이상하네");
                        destination.setContents(updatedContent);

                        // 대표 이미지 설정
                        String imageUrl = mediaProcessor.extractImageUrl(updatedContent);
                        if (imageUrl != null) {
                            destination.setImageUrl(imageUrl);
                        }
                        break;
                    case "featuresString":
                        List<String> features = Arrays.stream(newValue.split("#"))
                                .filter(tag -> !tag.isEmpty()) // 빈 문자열 제외
                                .map(tag -> "#" + tag) // 각 태그 앞에 # 추가
                                .collect(Collectors.toList());
                        destination.setFeatures(features);
                        break;
                    case "userRating":
                        Double userRating = Double.valueOf(newValue);
                        destination.addRating(userRating);
                        break;
                    case "recommendedSeason":
                        List<String> bestSeason = Arrays.stream(newValue.split(","))
                                .filter(tag -> !tag.isEmpty()) // 빈 문자열 제외
                                .collect(Collectors.toList());
                        destination.setBestSeason(bestSeason);
                        break;
                    // 다른 필드에 대한 업데이트 케이스 추가 가능
                    default:
                        return ResponseEntity.badRequest().body("지원하지 않는 업데이트 필드입니다.");
                }
            }

            // 3. 업데이트된 여행지 정보를 저장합니다.
            Destination updatedDestination = destinationService.saveDestination(destination);

            // 4. 업데이트된 여행지 정보를 클라이언트에게 성공 응답(200 OK)으로 반환합니다.
            return ResponseEntity.ok(updatedDestination);
        } catch (Exception e) {
            // 업데이트 중 오류가 발생한 경우 예외를 처리합니다.
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    // 특정 ID의 여행지를 삭제하는 엔드포인트
    @Operation(summary = "특정 ID의 여행지 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id) {
        // 1. 여행지 서비스를 통해 ID에 해당하는 여행지를 삭제합니다.
        destinationService.deleteDestination(id);

        // 2. 삭제가 성공적으로 수행되면 클라이언트에게 성공 응답(204 No Content)을 반환합니다.
        return ResponseEntity.noContent().build();
    }

    // 인기 있는 여행지를 추천하는 엔드포인트
    @Operation(summary = "인기 있는 (평점높은순) 여행지 추천")
    @GetMapping("/recommend/popular")
    public List<Destination> getPopularDestinations(@RequestParam int topN) {
        // 1. 사용자 서비스를 통해 인기 있는 여행지를 추천받습니다.
        List<Destination> recommendedDestinations = recommendationService.recommendPopularDestinations(topN);

        // 2. 추천된 여행지 목록을 클라이언트에게 반환합니다.
        return recommendedDestinations;
    }
}
