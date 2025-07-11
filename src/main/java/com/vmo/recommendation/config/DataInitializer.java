package com.vmo.recommendation.config;

import com.vmo.recommendation.entity.Trip;
import com.vmo.recommendation.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final TripRepository tripRepository;

    @Override
    public void run(String... args) throws Exception {
        tripRepository.deleteAll();
        Set<Trip> trips = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/trip-tracking.csv")))) {
            reader.lines().skip(1).forEach(line -> {
                String[] parts = line.split(",");
                trips.add(new Trip(null, parts[0], parts[1]));
            });
        }
        tripRepository.saveAll(trips);
    }
}
