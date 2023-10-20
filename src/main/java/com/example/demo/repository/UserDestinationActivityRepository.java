package com.example.demo.repository;

import com.example.demo.entity.UserDestinationActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDestinationActivityRepository extends JpaRepository<UserDestinationActivity, Long> {
    // 필요한 쿼리 메서드를 여기에 추가...
}
