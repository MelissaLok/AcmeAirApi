package com.acmeair.acmeairapi.controller;

import com.acmeair.acmeairapi.domain.Flight;
import com.acmeair.acmeairapi.service.FlightService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing flight-related operations.
 */
@RestController
@RequestMapping("/api/flights")
@Validated
public class FlightController {

    private final FlightService service;

    public FlightController(FlightService service) {
        this.service = service;
    }

    /**
     * Handles HTTP GET requests for searching available flights.
     * Endpoint: /api/flights/search
     *
     * @param origin       IATA code of the departure airport (e.g. "WLG").
     * @param destination  IATA code of the destination airport (e.g. "AKL").
     * @return 200 OK response containing a list of matching flights.
     */
    @GetMapping("/search")
    public ResponseEntity<List<Flight>> searchFlights(
            @RequestParam @NotBlank String origin,
            @RequestParam @NotBlank String destination
    ) {
        var results = service.searchFlights(origin, destination);
        return ResponseEntity.ok(results);
    }

    /**
     * Handles HTTP GET requests to fetch a specific flight by its ID.
     * Endpoint: /api/flights/{id}
     *
     * @param id Unique identifier of the flight.
     * @return 200 OK with the flight if found, otherwise 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable String id) {
        return service.getFlightById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}