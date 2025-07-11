package com.vmo.recommendation.service;

import com.vmo.recommendation.dto.SimilarityScore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheService {

    private Map<String, List<SimilarityScore>> similarityScores= new ConcurrentHashMap<>();


    public Map<String, List<SimilarityScore>> getMap() {
        return similarityScores;
    }
}
