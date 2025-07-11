package com.vmo.recommendation.service;

import com.vmo.recommendation.dto.SimilarityScore;
import com.vmo.recommendation.entity.RecommendationTrip;
import com.vmo.recommendation.repository.RecommendationTripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final CacheService cacheService;

    private final RecommendationTripRepository recommendationTripRepository;

    public List<SimilarityScore> recommend(String baseDestination) {

        Map<String, List<SimilarityScore>> similarityScoresMap = cacheService.getMap();

        if(similarityScoresMap.containsKey(baseDestination)) {
            return similarityScoresMap.get(baseDestination);
        }

        RecommendationTrip recommendationTrip = recommendationTripRepository.findById(baseDestination).orElse(null);
        if(recommendationTrip == null) return null;
        return recommendationTrip.getRecommendationTrips();
    }

}
