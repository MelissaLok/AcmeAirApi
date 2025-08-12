# ✈️ Acme Air API

A lightweight RESTful API for managing flights and passenger bookings. Built with Spring Boot 3.

---

## 🚀 Features

- Search available flights by origin and destination
- Create, retrieve, update, and cancel flight bookings
- In-memory data store (no external database required)
- Input validation with error responses
- Component and unit tests for core functionality

---

## 🛠️ Requirements

- Java 21
- Gradle (included via wrapper)
- Git (to clone the repo)

---

## ▶️ Running the Application

1. **Clone the repository & run the application**

```bash
git clone https://github.com/MelissaLok/AcmeAirApi.git
cd AcmeAirApi
./gradlew bootRun
```
Once it runs, it'll listen on `http://localhost:8080`


> Open Swagger has been enabled for ease of testing, 
but here are the steps on using the API via bash:

<br>

2.**🛫 Flight Endpoints**
- Search flights (GET)
```bash 
curl "http://localhost:8080/api/flights/search?origin=WLG&destination=AKL"
```

- Get Flight by ID (GET)
```bash 
curl "http://localhost:8080/api/flights/FL001"
```
<br>

3. **👨🏻‍💻 Booking Endpoints** 

- Create Booking (POST)
```bash
curl -X POST "http://localhost:8080/api/bookings" \
  -H "Content-Type: application/json" \
  -d '{
    "flightId": "FL001",
    "passenger": {
      "name": "Henry Cavil",
      "email": "henry@cavil.com",
      "phone": "021-000123"
    }
  }'
```
Expected: `201 Created`, with booking JSON in response.

Example response:
```
{
  "id": "47822eac-c2dc-4e03-a084-019d7a7ddb98",
  "flightId": "FL001",
  "passenger": {
    "name": "Henry Cavil",
    "email": "henry@cavil.com",
    "phone": "01234458"
  },
  "bookedAt": "2025-08-12T16:51:27.7901159",
  "status": "CONFIRMED"
}
```

<br>

- Get Booking by ID (GET)
```bash
curl "http://localhost:8080/api/bookings/$BOOKING_ID"
```
where $BOOKING_ID is the ID created in the previous step. 

Example booking ID (UUID): `47822eac-c2dc-4e03-a084-019d7a7ddb98`

<br>

- Update Passenger Info 
```bash
curl -X PUT "http://localhost:8080/api/bookings/$BOOKING_ID/passenger" \
  -H "Content-Type: application/json" \
  -d '{
    "passenger": {
      "name": "New Shiny Name",
      "email": "updated@email.com",
      "phone": "021-9876543"
    }
  }'
```

- Cancel Booking
```bash
curl "http://localhost:8080/$BOOKING_ID/cancel"
```
Expected: `200 OK` and booking status changes from `CONFIRMED` to `CANCELLED`

---

## 🧪 Running Tests
The project includes both unit and component tests.
> Make sure you are in the project root directory before running these commands!

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
│   │   ├── controller/              # REST controllers
│   │   ├── domain/                  # Domain models (Flight, Booking, Passenger)
│   │   ├── service/                 # Business logic
│   │   ├── repository/              # In-memory repositories
│   │   └── AcmeAirApiApplication.java
│   └── resources/
│       └── application.properties   # Config
├── test/
│   └── java/com/acmeair/acmeairapi/
│       ├── controller/              # Component tests
│       └── service/                 # Unit tests
```

---

## 🧾 Notes and Assumptions

- Data is stored in memory and resets on restart.
- All timestamps are in local server time.
- No authentication is required (for testing purposes).
- Booking IDs are UUIDs generated on creation.
