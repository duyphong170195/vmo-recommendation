package com.vmo.recommendation.controller;

import com.vmo.recommendation.controller.request.TripCreateRequest;
import com.vmo.recommendation.dto.SimilarityScore;
import com.vmo.recommendation.entity.Trip;
import com.vmo.recommendation.repository.TripRepository;
import com.vmo.recommendation.service.RecommendationService;
import com.vmo.recommendation.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;
    private final RecommendationService recommendationService;

    @PostMapping("/trip")
    public ResponseEntity<Void> bookTrip(@RequestBody TripCreateRequest request) {
        tripService.create(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/destination/recommendations")
    public List<SimilarityScore> getRecommendations(@RequestParam String destination) {
        return recommendationService.recommend(destination);
    }
}
