package com.acmeair.acmeairapi.domain;

import java.time.LocalDateTime;

/**
 * Represents a scheduled flight including origin, destination, and time details.
 *
 * @param id            Unique flight identifier.
 * @param origin        Departure location.
 * @param destination   Arrival location.
 * @param departureTime Scheduled departure timestamp.
 * @param arrivalTime   Scheduled arrival timestamp.
 */

public record Flight(
        String id,
        String origin,
        String destination,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime
) {}