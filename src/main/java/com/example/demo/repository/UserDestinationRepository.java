package com.example.demo.repository;

import com.example.demo.entity.UserDestination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

@Repository
public interface UserDestinationRepository extends JpaRepository<UserDestination, Long> {
    List<UserDestination> findByUserId(Long userId);
    @Query("SELECT AVG(ud.rating) FROM UserDestination ud WHERE ud.destination.id = ?1")
    Double findAverageRatingByDestinationId(Long destinationId);
}
