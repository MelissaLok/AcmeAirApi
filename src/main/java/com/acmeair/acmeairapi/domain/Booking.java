package com.acmeair.acmeairapi.domain;

import java.time.LocalDateTime;

/**
 * Represents a passenger's reservation for a specific flight.
 *
 * @param id        Unique booking identifier.
 * @param flightId  ID of the flight being booked.
 * @param passenger Passenger details.
 * @param bookedAt  Timestamp when booking was created.
 * @param status    Current booking status.
 */
public record Booking(
        String id,
        String flightId,
        Passenger passenger,
        LocalDateTime bookedAt,
        BookingStatus status
) {}
