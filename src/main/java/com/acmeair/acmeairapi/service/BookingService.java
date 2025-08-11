package com.acmeair.acmeairapi.service;

import com.acmeair.acmeairapi.domain.Booking;
import com.acmeair.acmeairapi.domain.BookingStatus;
import com.acmeair.acmeairapi.domain.Passenger;
import com.acmeair.acmeairapi.repository.BookingRepository;
import com.acmeair.acmeairapi.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Business logic for creating and managing flight bookings.
 */
@Service
public class BookingService {

    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;

    public BookingService(FlightRepository flightRepository, BookingRepository bookingRepository) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Creates a booking if the flight exists.
     *
     * @param flightId ID of the flight to book.
     * @param passenger Passenger details.
     * @return Optional booking if successful.
     */
    public Optional<Booking> createBooking(String flightId, Passenger passenger) {
        // check that the flight exists
        var flight = flightRepository.findById(flightId);
        if (flight.isEmpty()) return Optional.empty();

        var booking = new Booking(
                BookingRepository.newId(),
                flightId,
                passenger,
                LocalDateTime.now(),
                BookingStatus.CONFIRMED
        );
        bookingRepository.save(booking);
        return Optional.of(booking);
    }

    /**
     * Updates the passenger information for an existing booking.
     *
     * @param bookingId ID of the booking to update.
     * @param passenger New passenger info.
     * @return Optional booking with updated passenger.
     */
    public Optional<Booking> updatePassenger(String bookingId, Passenger passenger) {
        return bookingRepository.updatePassenger(bookingId, passenger);
    }

    /**
     * Retrieves a booking by ID.
     *
     * @param id Booking identifier.
     * @return Optional booking if found.
     */
    public Optional<Booking> findById(String id) {
        return bookingRepository.findById(id);
    }

    /**
     * Cancels an existing booking.
     *
     * @param id Booking identifier.
     * @return Optional cancelled booking if found.
     */
    public Optional<Booking> cancel(String id) {
        return bookingRepository.cancel(id);
    }

    /**
     * Get all bookings
     *
     * @return A list of all bookings.
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.getAllBookings();
    }
}
