package com.acmeair.acmeairapi.controller;

import com.acmeair.acmeairapi.AcmeAirApiApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = AcmeAirApiApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class FlightControllerTest {

    @LocalServerPort
    int port;

    private final TestRestTemplate rest = new TestRestTemplate();

    private String baseUrl() {
        return "http://localhost:" + port + "/api/flights";
    }

    // ---------- Search endpoint tests ----------

    @Test
    @DisplayName("Search flights: success returns matching flights with 200")
    void searchFlights_success() {
        ResponseEntity<String> response = rest.getForEntity(
                baseUrl() + "/search?origin=Wellington&destination=Auckland",
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("\"id\":\"FL001\"");
        assertThat(response.getBody()).doesNotContain("\"id\":\"FL002\"");
    }

    @Test
    @DisplayName("Search flights: missing origin -> 400 Bad Request")
    void searchFlights_missingParam_failure() {
        ResponseEntity<String> response = rest.getForEntity(
                baseUrl() + "/search?destination=Auckland",
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // ---------- Get by ID endpoint tests ----------

    @Test
    @DisplayName("Get flight by ID: success returns flight with 200")
    void getFlightById_success() {
        ResponseEntity<String> response = rest.getForEntity(
                baseUrl() + "/FL001",
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("\"origin\":\"Wellington\"");
        assertThat(response.getBody()).contains("\"destination\":\"Auckland\"");
    }

    @Test
    @DisplayName("Get flight by ID: unknown ID -> 404 Not Found")
    void getFlightById_notFound_failure() {
        ResponseEntity<String> response = rest.getForEntity(
                baseUrl() + "/UNKNOWN",
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}