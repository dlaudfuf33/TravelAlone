package com.example.demo.repository;

import com.example.demo.entity.UserDestinationActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDestinationActivityRepository extends JpaRepository<UserDestinationActivity, Long> {
    List<UserDestinationActivity> findByUserId(Long userId);

}
