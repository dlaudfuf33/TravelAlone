package com.example.demo.service;

import com.example.demo.entity.Destination;
import com.example.demo.repository.DestinationRepository;
import com.example.demo.repository.UserDestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;
    private final UserDestinationRepository userDestinationRepository;

    // 생성자를 통한 의존성 주입
    @Autowired
    public DestinationService(DestinationRepository destinationRepository, UserDestinationRepository userDestinationRepository) {
        this.destinationRepository = destinationRepository;
        this.userDestinationRepository = userDestinationRepository;
    }

    // 모든 여행지 정보를 반환
    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    // ID를 기반으로 특정 여행지 정보를 반환
    public Optional<Destination> getDestinationById(Long id) {
        return destinationRepository.findById(id);
    }

    // 여행지 정보 저장
    @Transactional
    public Destination saveDestination(Destination destination) {
        return destinationRepository.save(destination);
    }

    // 여행지 정보 삭제
    @Transactional
    public void deleteDestination(Long id) {
        destinationRepository.deleteById(id);
    }

    // 매일 자정에 모든 여행지의 평균 평점 업데이트
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateAllAverageRatings() {
        List<Destination> allDestinations = destinationRepository.findAll();
        for (Destination destination : allDestinations) {
            updateAverageRatingForDestination(destination.getId());
        }
    }

    // 특정 여행지의 평균 평점 업데이트
    @Transactional
    public void updateAverageRatingForDestination(Long destinationId) {
        Double averageRating = userDestinationRepository.findAverageRatingByDestinationId(destinationId);
        if (averageRating != null) {
            Optional<Destination> optionalDestination = destinationRepository.findById(destinationId);
            if (optionalDestination.isPresent()) {
                Destination destination = optionalDestination.get();
                destination.setAverageRating(averageRating);
                destinationRepository.save(destination);
            }
        }
    }

    // 특정 여행지에 새로운 평점 추가
    @Transactional
    public void addRatingToDestination(Long destinationId, Double newRating) {
        Optional<Destination> optionalDestination = destinationRepository.findById(destinationId);
        if (optionalDestination.isPresent()) {
            Destination destination = optionalDestination.get();
            destination.addRating(newRating);
            destinationRepository.save(destination);
        } else {
            throw new IllegalArgumentException("유효하지 않은 여행지 ID: " + destinationId);
        }
    }

    // 특정 여행지의 평점 업데이트
    @Transactional
    public void updateRatingOfDestination(Long destinationId, Double oldRating, Double newRating) {
        Optional<Destination> optionalDestination = destinationRepository.findById(destinationId);
        if (optionalDestination.isPresent()) {
            Destination destination = optionalDestination.get();
            destination.updateRating(oldRating, newRating);
            destinationRepository.save(destination);
        } else {
            throw new IllegalArgumentException("유효하지 않은 여행지 ID: " + destinationId);
        }
    }
}
