package com.example.demo.service;

import com.example.demo.entity.Destination;
import com.example.demo.repository.DestinationRepository;
import com.example.demo.repository.UserDestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DestinationService {
    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private UserDestinationRepository userDestinationRepository;

    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    public Optional<Destination> getDestinationById(Long id) {
        return destinationRepository.findById(id);
    }

    public Destination saveDestination(Destination destination) {
        return destinationRepository.save(destination);
    }

    public void deleteDestination(Long id) {
        destinationRepository.deleteById(id);
    }
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateAllAverageRatings() {
        List<Destination> allDestinations = destinationRepository.findAll();
        for (Destination destination : allDestinations) {
            updateAverageRatingForDestination(destination.getId());
        }
    }
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
}
