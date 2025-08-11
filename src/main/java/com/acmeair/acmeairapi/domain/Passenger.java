package com.acmeair.acmeairapi.domain;

/**
 * Passenger details tied to a flight booking.
 *
 * @param name   Passenger’s name.
 * @param email  Contact email address.
 * @param phone  Phone number.
 */
public record Passenger(
        String name,
        String email,
        String phone
) {}