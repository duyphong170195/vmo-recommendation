package com.vmo.recommendation.job;

import com.vmo.recommendation.dto.SimilarityScore;
import com.vmo.recommendation.entity.RecommendationTrip;
import com.vmo.recommendation.entity.Trip;
import com.vmo.recommendation.repository.RecommendationTripRepository;
import com.vmo.recommendation.repository.TripRepository;
import com.vmo.recommendation.service.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleCalculateTest {

    @InjectMocks
    private ScheduleCalculate scheduleCalculate;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private RecommendationTripRepository recommendationTripRepository;

    @Mock
    private CacheService cacheService;

    @Mock
    private Map<String, List<SimilarityScore>> mockCacheMap;

    @BeforeEach
    void setup() {
        Mockito.lenient().when(cacheService.getMap()).thenReturn(mockCacheMap);
    }

    @Test
    void testHandleCalculate() {
        // Given
        Trip trip1 = new Trip("user1", "paris");
        Trip trip2 = new Trip("user2", "london");
        Trip trip3 = new Trip("user1", "london");
        Trip trip4 = new Trip("user3", "paris");

        List<Trip> trips = List.of(trip1, trip2, trip3, trip4);

        when(tripRepository.findAll()).thenReturn(trips);

        // When
        scheduleCalculate.handleCalculate();

        // Then
        ArgumentCaptor<List<RecommendationTrip>> captor = ArgumentCaptor.forClass(List.class);
        verify(recommendationTripRepository, atLeastOnce()).saveAll(captor.capture());

        // Check that cache is updated
        verify(cacheService.getMap(), atLeastOnce()).put(anyString(), anyList());

        List<RecommendationTrip> saved = captor.getAllValues().stream()
            .flatMap(Collection::stream).toList();

        assertFalse(saved.isEmpty(), "RecommendationTrips should be saved");

        for (RecommendationTrip rt : saved) {
            assertNotNull(rt.getDestination());
            assertNotNull(rt.getRecommendationTrips());
        }
    }

    @Test
    void testHandleCalculateWithCorrectSavedRecommendations() {
        // Arrange: two destinations, shared user user1
        Trip trip1 = new Trip("user1", "paris");
        Trip trip2 = new Trip("user2", "london");
        Trip trip3 = new Trip("user1", "london");
        Trip trip4 = new Trip("user3", "paris");

        List<Trip> trips = List.of(trip1, trip2, trip3, trip4);
        when(tripRepository.findAll()).thenReturn(trips);

        // Capture saved recommendations
        ArgumentCaptor<List<RecommendationTrip>> saveCaptor = ArgumentCaptor.forClass(List.class);

        // Act
        scheduleCalculate.handleCalculate();

        // Assert
        verify(recommendationTripRepository, atLeastOnce()).saveAll(saveCaptor.capture());

        // Flatten all saved batches
        List<RecommendationTrip> allSaved = saveCaptor.getAllValues().stream()
                .flatMap(Collection::stream)
                .toList();

        // Check that paris has a recommendation to london with score 0.5
        RecommendationTrip parisTrip = allSaved.stream()
                .filter(r -> r.getDestination().equals("paris"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Missing paris RecommendationTrip"));

        SimilarityScore parisToLondon = parisTrip.getRecommendationTrips().stream()
                .filter(s -> s.getDestination().equals("london"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Missing paris to london similarity"));

        assertEquals(0.5, parisToLondon.getSimilarityScore(), 0.0001);

        // Check london has paris
        RecommendationTrip londonTrip = allSaved.stream()
                .filter(r -> r.getDestination().equals("london"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Missing london RecommendationTrip"));

        SimilarityScore londonToParis = londonTrip.getRecommendationTrips().stream()
                .filter(s -> s.getDestination().equals("paris"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Missing london to paris similarity"));

        assertEquals(0.5, londonToParis.getSimilarityScore(), 0.0001);
    }
}
