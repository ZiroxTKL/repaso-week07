package com.example.week07.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class Dtos {
    public static class NewIdDTO { public UUID id; public NewIdDTO() {} public NewIdDTO(UUID id){this.id=id;} }

    public static class NewFlightRequestDTO {
        public String airlineName;
        public String flightNumber;
        public OffsetDateTime estDepartureTime;
        public OffsetDateTime estArrivalTime;
        public Integer availableSeats;
    }

    public static class RegisterUserDTO {
        public String firstName;
        public String lastName;
        public String email;
        public String password;
    }

    public static class LoginDTO {
        public String email;
        public String password;
    }

    public static class AuthToken { public String token; public AuthToken(){} public AuthToken(String t){this.token=t;} }

    public static class FlightResponseDTO {
        public UUID id;
        public String airlineName;
        public String flightNumber;
        public OffsetDateTime estDepartureTime;
        public OffsetDateTime estArrivalTime;
        public Integer availableSeats;
    }

    public static class FlightSearchResponseDTO {
        public List<FlightResponseDTO> flights;
    }

    public static class NewFlightManyRequestDTO {
        public List<NewFlightRequestDTO> flights;
    }

    public static class NewFlightManyResponseDTO {
        public int created;
    }

    public static class FlightBookRequestDTO {
        public UUID flightId;
    }

    public static class BookingInfoDTO {
        public UUID id;
        public OffsetDateTime bookingDate;
        public UUID flightId;
        public String flightNumber;
        public UUID customerId;
        public String customerFirstName;
        public String customerLastName;
    }
}
