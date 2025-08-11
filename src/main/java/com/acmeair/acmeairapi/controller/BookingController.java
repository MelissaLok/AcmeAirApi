package com.acmeair.acmeairapi.controller;

import com.acmeair.acmeairapi.domain.Booking;
import com.acmeair.acmeairapi.domain.Passenger;
import com.acmeair.acmeairapi.service.BookingService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

import java.net.URI;

/**
 * REST controller exposing booking-related endpoints.
 */
@RestController
@RequestMapping("/api/bookings")
@Validated
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    /**
     * Request payload for creating a new booking.
     * Contains flight identifier and passenger details.
     *
     * @param flightId  Unique ID representing the flight to be booked.
     * @param passenger Passenger information including name, email, and phone.
     */
    public record CreateBookingRequest(
            @NotBlank String flightId,
            @Valid PassengerDto passenger
    ) {}

    /**
     * Request payload for updating an existing passenger's information.
     *
     * @param passenger Updated passenger details.
     */
    public record UpdatePassengerRequest(
            @Valid PassengerDto passenger
    ) {}

    /**
     * Data Transfer Object representing a passenger.
     * Includes basic contact details and provides a helper method
     * to convert to domain model type {@link Passenger}.
     *
     * @param name  Passenger name.
     * @param email Passenger of valid format.
     * @param phone Passenger phone number.
     */
    public record PassengerDto(
            @NotBlank String name,
            @NotBlank @Email String email,
            @NotBlank String phone
    ) {
        /**
         * Converts this DTO to a domain model object.
         *
         * @return a {@link Passenger} instance populated from this DTO.
         */
        public Passenger toModel() {
            return new Passenger(name, email, phone);
        }
    }

    /**
     * Creates a booking for a passenger on a given flight.
     * Method: POST
     * Endpoint: /api/bookings
     *
     * @param request CreateBookingRequest containing flight ID and passenger info.
     * @return 201 Created with booking details or 404 if flight not found.
     */
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody @Valid CreateBookingRequest request) {
        return service.createBooking(request.flightId(), request.passenger().toModel())
                .map(created -> {
                    var location = URI.create("/api/bookings/" + created.id());
                    return ResponseEntity.created(location).body(created);
                })
                .orElseGet(() -> ResponseEntity.notFound().build()); // flightId not found
    }

    /**
     * Retrieves a booking by ID.
     *
     * @param id Booking identifier.
     * @return 200 OK with booking or 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates the passenger info for an existing booking.
     * Method: PUT
     * Endpoint: /api/bookings/{id}/passenger
     *
     * @param id Booking identifier.
     * @param request Passenger update payload.
     * @return 200 OK with updated booking or 404 if not found.
     */
    @PutMapping("/{id}/passenger")
    public ResponseEntity<Booking> updatePassenger(
            @PathVariable String id,
            @RequestBody @Valid UpdatePassengerRequest request
    ) {
        return service.updatePassenger(id, request.passenger().toModel())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Cancels an active booking.
     * Method: POST
     * Endpoint: /api/bookings/{id}/cancel
     *
     * @param id Booking identifier.
     * @return 200 OK with cancelled booking or 404 if not found.
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancel(@PathVariable String id) {
        return service.cancel(id)
                .map(ResponseEntity::ok) // 200 OK with the cancelled (or already-cancelled) booking
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404 if booking doesn't exist
    }
}