package com.acmeair.acmeairapi.repository;

import com.acmeair.acmeairapi.domain.Booking;
import com.acmeair.acmeairapi.domain.BookingStatus;
import com.acmeair.acmeairapi.domain.Passenger;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory booking repository supporting CRU(D). (D) is cancellation.
 */
@Component
public class BookingRepository {

    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();

    /**
     * Stores a new booking record.
     *
     * @param booking Booking to persist.
     */
    public void save(Booking booking) {
        bookings.put(booking.id(), booking);
    }

    /**
     * Fetches a booking by ID.
     *
     * @param id Booking identifier.
     * @return Optional booking if found.
     */
    public Optional<Booking> findById(String id) {
        return Optional.ofNullable(bookings.get(id));
    }

    /**
     * Updates passenger info for a given booking.
     *
     * @param bookingId ID of the booking to update.
     * @param newPassenger Updated passenger details.
     * @return Optional updated booking if successful.
     */
    public Optional<Booking> updatePassenger(String bookingId, Passenger newPassenger) {
        var existing = bookings.get(bookingId);
        if (existing == null) return Optional.empty();
        var updated = new Booking(
                existing.id(),
                existing.flightId(),
                newPassenger,
                existing.bookedAt(),
                existing.status()
        );
        bookings.put(updated.id(), updated);
        return Optional.of(updated);
    }

    /**
     * Cancels a booking by changing its status.
     *
     * @param bookingId ID of the booking to cancel.
     * @return Optional cancelled booking if found.
     */
    public Optional<Booking> cancel(String bookingId) {
        var existing = bookings.get(bookingId);
        if (existing == null) return Optional.empty();
        if (existing.status() == BookingStatus.CANCELLED) {
            return Optional.of(existing); // idempotent
        }
        var cancelled = new Booking(
                existing.id(),
                existing.flightId(),
                existing.passenger(),
                existing.bookedAt(),
                BookingStatus.CANCELLED
        );
        bookings.put(cancelled.id(), cancelled);
        return Optional.of(cancelled);
    }

    /**
     * Generates a new unique booking ID.
     *
     * @return UUID string.
     */
    public static String newId() {
        return UUID.randomUUID().toString();
    }

    /**
     * List all bookings.
     *
     * @return New list of all bookings.
     */
    public List<Booking> getAllBookings() {
        // return bookings.values()
        // ^ will expose live reference to the internal map's value collection o.o
        // returning new so original list not mutated
        return new ArrayList<>(bookings.values());
    }
}
