package com.vmo.recommendation.repository;

import com.vmo.recommendation.entity.RecommendationTrip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationTripRepository extends JpaRepository<RecommendationTrip, String> {
}
