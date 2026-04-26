# Car rental system
Car rental system with simple REST APIs to rent cars, return cars and get all customers, cars and rentals by customer

## Technology
- Java 21
- Spring Boot 4.0.6
- H2 Database
- Maven

## Run the project

```
./mvnw spring-boot:run
```

Application starts on:
- http://localhost:8080

H2 console:
- http://localhost:8080/h2-console
- jdbc url= jdbc:h2:mem:carrentaldb
- user= admin
- password= admin

## Data
- Sample data in data.sql with 6 cars (two of each type) and 2 customers
- Base price:
  - Premium: 300
  - SUV: 150
  - Small: 50
- Loyalty points:
  - Premium: 5 points
  - SUV: 3 points
  - Small: 1 point

## Endpoints

- Get cars
  - GET /cars
- Get customers
  - GET /customers
- Create rental
  - POST /rentals
- Return rental
  - POST /rentals/return/{rentalId}
- Get rentals by customer id
  - GET /rentals/customer/{customerId}

## Postman Collection
Files in /postman folder with the 5 endpoints

## Assumptions

- All cars are rented on the same date and are returned on the same date
- Pricing by full days, not hours.
- Billing and payments out of scope
- Domain models are also used as JPA entities (for simplicity, no hexagonal architecture)
- Early return is allowed, with no surcharge and no refund.
- Loyalty points were implemented directly in a enum, but a Strategy pattern, as pricing, could also be implemented

## Missing (due to simplicity or lack of time)

- Unit tests for Controllers
- Global exception handling with @RestControllerAdvice
- Response objects (instead of return the model in the API)
