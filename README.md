# Motravel - Vehicle Rental Application Backend

A Spring Boot backend for a vehicle rental application with JWT authentication, PostgreSQL database, and RESTful APIs.

## Features

- User Signup and Login with JWT Authentication
- Vehicle management (CRUD operations)
- Booking system with availability checks
- Location-based vehicle search using Haversine formula
- User booking history
- Swagger/OpenAPI documentation

## Tech Stack

- **Framework**: Spring Boot 3.1.5
- **Database**: PostgreSQL
- **Authentication**: JWT
- **API Type**: REST
- **Documentation**: Swagger/OpenAPI

## Prerequisites

- Java 17 or higher
- PostgreSQL database
- Maven

## Setup and Installation

1. Clone the repository
2. Configure PostgreSQL database
   - Create a database named `motravel`
   - Update `application.properties` with your database credentials if needed
3. Build the project: `mvn clean install`
4. Run the application: `mvn spring-boot:run`

## API Documentation

Once the application is running, you can access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## API Endpoints

### Authentication
- `POST /api/auth/signup` - Register a new user
- `POST /api/auth/signin` - Login and get JWT token

### Vehicles
- `GET /api/vehicles` - Get all vehicles
- `GET /api/vehicles/{id}` - Get vehicle by ID
- `GET /api/vehicles/available` - Get all available vehicles
- `GET /api/vehicles/nearby` - Find vehicles near a location (within 5km radius)
- `POST /api/vehicles` - Add a new vehicle (Admin only)
- `PUT /api/vehicles/{id}` - Update a vehicle (Admin only)
- `PATCH /api/vehicles/{id}/availability` - Update vehicle availability (Admin only)
- `DELETE /api/vehicles/{id}` - Delete a vehicle (Admin only)

### Bookings
- `GET /api/bookings` - Get all bookings (Admin only)
- `GET /api/bookings/{id}` - Get booking by ID
- `GET /api/bookings/user` - Get bookings for the current user
- `POST /api/bookings` - Create a new booking
- `PATCH /api/bookings/{id}/status` - Update booking status (Admin only)
- `POST /api/bookings/{id}/cancel` - Cancel a booking
- `GET /api/bookings/check-availability` - Check if a vehicle is available for booking

## Test Users

The application is pre-loaded with test users:

1. Admin User:
   - Username: admin
   - Password: admin123
   - Roles: ROLE_ADMIN, ROLE_USER

2. Regular User:
   - Username: user
   - Password: user123
   - Role: ROLE_USER

## Test Vehicles

Several test vehicles are pre-loaded in different locations around Delhi, India.
