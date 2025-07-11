package com.vmo.recommendation.repository;

import com.vmo.recommendation.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByUserId(String userId);
    List<Trip> findByDestination(String destination);
}
