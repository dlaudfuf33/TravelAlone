package com.example.demo.repository;

import com.example.demo.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {
    Optional<SystemConfig> findTopByOrderByModifiedDateDesc();
}
