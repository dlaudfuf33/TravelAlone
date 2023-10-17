package com.example.demo.service;

import com.example.demo.entity.UserDestination;
import com.example.demo.repository.UserDestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDestinationService {

    @Autowired
    private UserDestinationRepository userDestinationRepository;

    public List<UserDestination> getAllUserDestinations() {
        return userDestinationRepository.findAll();
    }

    public Optional<UserDestination> getUserDestinationById(Long id) {
        return userDestinationRepository.findById(id);
    }

    public UserDestination saveUserDestination(UserDestination userDestination) {
        return userDestinationRepository.save(userDestination);
    }

    public void deleteUserDestination(Long id) {
        userDestinationRepository.deleteById(id);
    }
}
