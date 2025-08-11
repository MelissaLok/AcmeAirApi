package com.acmeair.acmeairapi.service;

import com.acmeair.acmeairapi.domain.Flight;
import com.acmeair.acmeairapi.repository.FlightRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlightServiceTest {
    @Test
    void search_filtersAndSorts() {
        var storage = new FlightRepository();
        storage.init();
        var service = new FlightService(storage);

        var results = service.searchFlights("wellington", "auckland");
        assertFalse(results.isEmpty());
        assertTrue(results.stream().allMatch(f ->
                f.origin().equalsIgnoreCase("Wellington") && f.destination().equalsIgnoreCase("Auckland")));

        var times = results.stream().map(Flight::departureTime).toList();
        assertTrue(java.util.stream.IntStream.range(1, times.size()).noneMatch(i -> times.get(i).isBefore(times.get(i-1))));
    }
}