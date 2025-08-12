# ✈️ Acme Air API

A lightweight RESTful API for managing flights and passenger bookings. Built with Spring Boot and in-memory storage for fast prototyping and testing.

---

## 🚀 Features

- Search available flights by origin and destination
- Create, retrieve, update, and cancel flight bookings
- In-memory data store (no external database required)
- Input validation with clear error responses
- Component and unit tests for core functionality

---

## 🛠️ Requirements

- Java 21+
- Gradle (included via wrapper)
- Git (to clone the repo)

---

## ▶️ Running the Application

1. **Clone the repository**

```bash
git clone https://github.com/MelissaLok/AcmeAirApi.git
cd AcmeAirApi
./gradlew bootRun
```

---

## 🧪 Running Tests
The project includes both unit and component tests.

To run all tests:

```bash
./gradlew test
```

To run a specific test:

```bash
./gradlew test --tests "com.acmeair.acmeairapi.service.FlightServiceTest"
```

Test coverage includes:

- Booking creation, retrieval, update, cancellation
- Flight search and lookup
- Validation and error handling

---

## 📦 Project Structure

```
src/
├── main/
│   ├── java/com/acmeair/acmeairapi/
│   │   ├── controller/       # REST controllers
│   │   ├── domain/           # Domain models (Flight, Booking, Passenger)
│   │   ├── service/          # Business logic
│   │   ├── repository/       # In-memory repositories
│   │   └── AcmeAirApiApplication.java
│   └── resources/
│       └── application.yml   # Config
├── test/
│   └── java/com/acmeair/acmeairapi/
│       ├── controller/       # Component tests
│       └── service/          # Unit tests
```

---

## 🧾 Notes and Assumptions

- Data is stored in memory and resets on restart.
- All timestamps are in local server time.
- No authentication is required (for testing purposes).
- Booking IDs are UUIDs generated on creation.
