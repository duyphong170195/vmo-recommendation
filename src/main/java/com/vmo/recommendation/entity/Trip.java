package com.vmo.recommendation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Trip {
    @Id
    @GeneratedValue
    private Long id;
    private String userId;
    private String destination;

    public Trip(String userId, String destination) {
        this.userId = userId;
        this.destination = destination;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equals(userId, trip.userId) && Objects.equals(destination, trip.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, destination);
    }
}
