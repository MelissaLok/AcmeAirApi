package com.acmeair.acmeairapi.controller;

import com.acmeair.acmeairapi.AcmeAirApiApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(
        classes = AcmeAirApiApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class BookingControllerTest {
    @LocalServerPort
    int port;

    private final TestRestTemplate rest = new TestRestTemplate();

    private String baseUrl() {
        return "http://localhost:" + port + "/api/bookings";
    }

    // ---------- Create booking tests ----------

    @Test
    @DisplayName("Create booking: success -> 201 Created and body contains booking ID")
    void createBooking_success() {
        String body = """
                {
                  "flightId": "FL001",
                  "passenger": {
                    "name": "Alex Doe",
                    "email": "alex@example.com",
                    "phone": "+64-021-0000"
                  }
                }
                """;

        ResponseEntity<String> response = rest.postForEntity(baseUrl(), entity(body), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        assertThat(response.getBody()).contains("\"flightId\":\"FL001\"");
        assertThat(response.getBody()).contains("\"name\":\"Alex Doe\"");
    }

    @Test
    @DisplayName("Create booking: unknown flight -> 404 Not Found")
    void createBooking_unknownFlight_failure() {
        String body = """
                {
                  "flightId": "UNKNOWN",
                  "passenger": {
                    "name": "Casey",
                    "email": "casey@example.com",
                    "phone": "021-123"
                  }
                }
                """;

        ResponseEntity<String> response = rest.postForEntity(baseUrl(), entity(body), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // ---------- Update passenger tests ----------

    @Test
    @DisplayName("Update passenger: success -> 200 and passenger replaced")
    void updatePassenger_success() {
        // First create a booking
        String createBody = """
                {
                  "flightId": "FL001",
                  "passenger": {
                    "name": "Initial Name",
                    "email": "initial@example.com",
                    "phone": "021-111"
                  }
                }
                """;
        ResponseEntity<String> createResp = rest.postForEntity(baseUrl(), entity(createBody), String.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertNotNull(createResp.getBody());
        String bookingId = extract(createResp.getBody(), "\"id\":\"", "\"");

        // Now update passenger
        String updateBody = """
                {
                  "passenger": {
                    "name": "Updated Name",
                    "email": "updated@example.com",
                    "phone": "021-222"
                  }
                }
                """;
        ResponseEntity<String> updateResp = rest.exchange(
                baseUrl() + "/" + bookingId + "/passenger",
                HttpMethod.PUT,
                entity(updateBody),
                String.class
        );

        assertThat(updateResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResp.getBody()).contains("\"name\":\"Updated Name\"");
        assertThat(updateResp.getBody()).contains("\"email\":\"updated@example.com\"");
    }

    @Test
    @DisplayName("Update passenger: booking not found -> 404 Not Found")
    void updatePassenger_bookingNotFound_failure() {
        String updateBody = """
                {
                  "passenger": {
                    "name": "Nobody",
                    "email": "nobody@example.com",
                    "phone": "021-000"
                  }
                }
                """;

        ResponseEntity<String> updateResp = rest.exchange(
                baseUrl() + "/NON_EXISTENT/passenger",
                HttpMethod.PUT,
                entity(updateBody),
                String.class
        );

        assertThat(updateResp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // ---------- Cancel Tests ----------
    @Test
    @DisplayName("Cancel booking: success -> 200 and status=CANCELLED")
    void cancelBooking_success() {
        // Create a booking first
        String createBody = """
                {
                  "flightId": "FL001",
                  "passenger": {
                    "name": "Will Cancel",
                    "email": "cancelme@example.com",
                    "phone": "021-333"
                  }
                }
                """;
        ResponseEntity<String> createResp = rest.postForEntity(baseUrl(), entity(createBody), String.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertNotNull(createResp.getBody());
        String bookingId = extract(createResp.getBody(), "\"id\":\"", "\"");

        // Cancel it
        ResponseEntity<String> cancelResp = rest.postForEntity(baseUrl() + "/" + bookingId + "/cancel", null, String.class);
        assertThat(cancelResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(cancelResp.getBody()).contains("\"status\":\"CANCELLED\"");

        // Idempotent: cancel again still 200 and CANCELLED
        ResponseEntity<String> cancelAgain = rest.postForEntity(baseUrl() + "/" + bookingId + "/cancel", null, String.class);
        assertThat(cancelAgain.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(cancelAgain.getBody()).contains("\"status\":\"CANCELLED\"");
    }

    @Test
    @DisplayName("Cancel booking: not found -> 404")
    void cancelBooking_notFound() {
        ResponseEntity<String> cancelResp = rest.postForEntity(baseUrl() + "/NON_EXISTENT/cancel", null, String.class);
        assertThat(cancelResp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    // ---------- Helpers ----------

    private HttpEntity<?> entity(String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    // naive extractor for tests
    private String extract(String body, String prefix, String suffix) {
        int i = body.indexOf(prefix);
        if (i < 0) return null;
        int start = i + prefix.length();
        int end = body.indexOf(suffix, start);
        return end > start ? body.substring(start, end) : null;
    }
}
