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
If you get a permission denied error when running `./gradlew bootRun`,
then please run chmod +x gradlew
(Likely to hit if you are running from a virtual environment).

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

- Get All Available Flights (GET)
```bash 
curl "http://localhost:8080/api/flights"
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
curl -X POST \
  'http://localhost:8080/api/bookings/$BOOKING_ID/cancel' \
  -H 'accept: */*' \
  -d ''
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

## 🤕 Trade Offs and Assumptions

1. *Simplicity vs Flexibility*
- A simple `PUT` is easy to use, but it may not support partial updates when using `PATCH`

2. *Schema evolution vs Backward Compatibility*
- Changing passenger update later may cause older clients to break, will require versioning.

3. *External DB vs In-memory storage*
- No (external) database means a restart resets everything 

4. *CRU(D)*
- Cancellation is a soft status change, not a deletion.
- We don't actually want to delete the resource, and POST gives me the flexibility for real-world cancellation logic
- in most systems, cancelled bookings are retained for audit, reporting and/or reactivation.
- Allows extending the logic, for example: 
  - refund
  - inventory updates
  - email notifications
- however! POST is not idempotent unlike DELETE 

5. *Validation is handled via annotations (i.e @NotBlank, @Email)*
- Not done with manual checks; which means this is a "valid" email: abc@IkP.com
- Can consider regex to handle emails
- `@Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
  flags = Pattern.Flag.CASE_INSENSITIVE)`

6. *Generic Error Handling*
- Can be extended via @ControllerAdvice

7. Booking ID format is UUID based and created on POST.

8. Time is done in local server time, using LocalDateTime; so there is no timezone normalization.

9. Passenger info is embedded directly in the booking payload, not stored separately.

10. Concurrency is not handled
    - if two users try to update the same passenger at the same time, we will get race conditions. 
    - Will require optimistic locking or versioning to prevent this from happening.

---

## 🚫 Features Left Out (deliberately)

1. Pricing and Payment Flow
- fare calculations, taxes, discounts, refunds etc require integration with pricing engines and payment gateways
- without this we have no dynamic pricing, no refund logic, and no payment processing

2. Seat Map / Inventory Management 
- this requires managing seat counts, capacity, and overbooking (which adds significant complexity)
- will cause overbooking/double booking

3. PATCH Partial Updating
- requires more sophisticated validation and merging logic
- without this, we risk accidental data loss if fields are omitted

4. Security
- even though essential, this is an infrastructure concern
- can cause data leaks and unauthorized access 

5. Search by Passenger
- this requires indexing and also normalization of passenger data 

6. Only Relying on @Valid annotations
- input sanitization is not implemented 
- api is prone to XSS and/or SQL injections

7. Pagination
- much like pricing and inventory management, adds too much complexity especially during testing and mocking
- data size is inversely proportional to performance to scalability (data up, performance & scalability down)