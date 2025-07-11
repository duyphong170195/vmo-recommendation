package com.vmo.recommendation.controller;

import com.vmo.recommendation.controller.request.TripCreateRequest;
import com.vmo.recommendation.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trip")
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;

    @PostMapping("")
    public ResponseEntity<Void> bookTrip(@RequestBody TripCreateRequest request) {
        tripService.create(request);
        return ResponseEntity.ok().build();
    }
}
