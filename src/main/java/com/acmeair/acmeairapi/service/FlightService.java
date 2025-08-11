package com.acmeair.acmeairapi.service;

import com.acmeair.acmeairapi.domain.Flight;
import com.acmeair.acmeairapi.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    private final FlightRepository storage;

    public FlightService(FlightRepository storage) {
        this.storage = storage;
    }

    public List<Flight> searchFlights(String origin, String destination) {
        return storage.search(origin, destination);
    }

    public Optional<Flight> getFlightById(String id) {
        return storage.findById(id);
    }
}
