package com.acmeair.acmeairapi.repository;

import com.acmeair.acmeairapi.domain.Flight;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

/**
 * In-memory storage and retrieval for available flights.
 */
@Component
public class FlightRepository {

    /**
     * Initializes the flight store with deterministic test data.
     */
    private final Map<String, Flight> flights = new HashMap<>();

    @PostConstruct
    public void init(){
        replaceAll(defaultSeed());
    }

    /**
     * Replaces all existing flights with the given list.
     *
     * @param seed List of flights to load into memory.
     */
    public void replaceAll(Collection<Flight> seed){
        flights.clear();
        seed.forEach(flight -> flights.put(flight.id(), flight));
    }

    /**
     * Returns default fixture flights for testing.
     *
     * @return List of predefined flights.
     */
    List<Flight> defaultSeed() {
        LocalDateTime base = LocalDateTime.of(2025, 8, 11, 9, 0);
        return List.of(
                new Flight("FL001", "Wellington", "Auckland",     base.plusHours(2), base.plusHours(3)),
                new Flight("FL002", "Wellington", "Christchurch", base.plusHours(4), base.plusHours(5)),
                new Flight("FL003", "Auckland",   "Wellington",   base.plusHours(6), base.plusHours(7))
        );
    }

    /**
     * Searches for flights matching origin and destination.
     *
     * @param origin Departure location.
     * @param destination Arrival location.
     * @return Sorted list of matching flights.
     */
    public List<Flight> search(String origin, String destination) {
        return flights.values().stream()
                .filter(f -> f.origin().equalsIgnoreCase(origin))
                .filter(f -> f.destination().equalsIgnoreCase(destination))
                .sorted(Comparator.comparing(Flight::departureTime))
                .toList();
    }

    /**
     * Retrieves a flight by its ID.
     *
     * @param id Flight identifier.
     * @return Optional containing the flight if found.
     */
    public Optional<Flight> findById(String id) {
        return Optional.ofNullable(flights.get(id));
    }
}