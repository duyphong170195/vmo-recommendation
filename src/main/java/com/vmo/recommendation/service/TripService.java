package com.vmo.recommendation.service;

import com.vmo.recommendation.controller.request.TripCreateRequest;
import com.vmo.recommendation.mapper.TripMapper;
import com.vmo.recommendation.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;


    public void create(TripCreateRequest request) {
        tripRepository.save(TripMapper.INSTANCE.toEntity(request));
    }

}
