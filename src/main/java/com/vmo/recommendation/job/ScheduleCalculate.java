package com.vmo.recommendation.job;

import com.vmo.recommendation.dto.SimilarityScore;
import com.vmo.recommendation.entity.RecommendationTrip;
import com.vmo.recommendation.entity.Trip;
import com.vmo.recommendation.repository.RecommendationTripRepository;
import com.vmo.recommendation.repository.TripRepository;
import com.vmo.recommendation.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ScheduleCalculate {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private RecommendationTripRepository recommendationTripRepository;

    @Autowired
    private CacheService cacheService;

    @Scheduled(cron = "0 */3 * * * *")
    public void handleCalculate() {
        log.info("================ recalculate recommendation trip ===============");
        List<Trip> trips = tripRepository.findAll();

        Map<String, Set<String>> destinationToUsers = new ConcurrentHashMap<>();
        for (Trip trip : trips) {
            destinationToUsers
                    .computeIfAbsent(trip.getDestination(), k -> new HashSet<>())
                    .add(trip.getUserId());
        }

        List<RecommendationTrip> recommendationTrips = new ArrayList<>();
        destinationToUsers.forEach((baseDestination, value) -> {
            Set<String> baseUsers = destinationToUsers.getOrDefault(baseDestination, Set.of());

            List<SimilarityScore> similarityScores = new ArrayList<>();
            for (String dest : destinationToUsers.keySet()) {
                if (dest.equals(baseDestination)) continue;

                Set<String> users = destinationToUsers.get(dest);
                double dot = baseUsers.stream().filter(users::contains).count();
                double denom = Math.sqrt(baseUsers.size()) * Math.sqrt(users.size());

                if (denom != 0) {
                    similarityScores.add(new SimilarityScore(dest, dot / denom));
                }
            }
            similarityScores.sort(Comparator.comparing(SimilarityScore::getSimilarityScore).reversed());
            cacheService.getMap().put(baseDestination, similarityScores);
            recommendationTrips.add(new RecommendationTrip(baseDestination, similarityScores));
            if(recommendationTrips.size() / 20 == 1) {
                recommendationTripRepository.saveAll(recommendationTrips);
                recommendationTrips.clear();
            }
        });
        if(recommendationTrips.size() > 0) {
            recommendationTripRepository.saveAll(recommendationTrips);
        }
    }
}
