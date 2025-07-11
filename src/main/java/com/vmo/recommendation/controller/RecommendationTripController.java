package com.vmo.recommendation.controller;

import com.vmo.recommendation.dto.SimilarityScore;
import com.vmo.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommendation-trip")
@RequiredArgsConstructor
public class RecommendationTripController {
    private final RecommendationService recommendationService;

    @GetMapping("/recommendation")
    public List<SimilarityScore> getRecommendations(@RequestParam String destination) {
        return recommendationService.recommend(destination);
    }
}
