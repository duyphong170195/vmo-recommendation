package com.vmo.recommendation.entity;

import com.vmo.recommendation.dto.SimilarityScore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecommendationTrip {

    @Id
    private String destination;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<SimilarityScore> recommendationTrips;
}
