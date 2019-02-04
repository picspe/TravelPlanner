package com.pepic.TravelPlanner.repositories;

import com.pepic.TravelPlanner.models.Trip;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface TripRepository extends CrudRepository<Trip, Long> {

    @Query(value = "SELECT * FROM trips " +
            "WHERE user_id = :username", nativeQuery = true)
    Collection<Trip> getAllTripsForUser(String username);

    @Query(value = "SELECT * FROM trips " +
            "WHERE user_id = :username " +
            "AND destination = :destination", nativeQuery = true)
    Collection<Trip> getAllTripsByDestination(String username, String destination);

    @Query(value = "DELETE * FROM trips " +
            "WHERE user_id = :username " +
            "AND id = :id", nativeQuery = true)
    void deleteByIdForUser(Long id, String username);
}
