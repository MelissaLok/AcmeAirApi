package com.acmeair.acmeairapi.service;

import com.acmeair.acmeairapi.domain.Booking;
import com.acmeair.acmeairapi.domain.BookingStatus;
import com.acmeair.acmeairapi.domain.Passenger;
import com.acmeair.acmeairapi.repository.BookingRepository;
import com.acmeair.acmeairapi.repository.FlightRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class BookingServiceTest {
    @Test
    void cancel_isIdempotent() {
        var flights = mock(FlightRepository.class);
        var storage = new BookingRepository();
        var service = new BookingService(flights, storage);

        var booking = new Booking(
                BookingRepository.newId(), "FL001",
                new Passenger("A", "a@example.com", "021"),
                java.time.LocalDateTime.now(),
                BookingStatus.CONFIRMED
        );
        storage.save(booking);

        var first = service.cancel(booking.id()).orElseThrow();
        assertEquals(BookingStatus.CANCELLED, first.status());

        var second = service.cancel(booking.id()).orElseThrow();
        assertEquals(BookingStatus.CANCELLED, second.status());
    }
}
