package com.example.week07.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "flights", indexes = {
        @Index(name = "idx_flight_flight_number", columnList = "flightNumber", unique = true)
})
public class Flight {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String airlineName;

    @Column(nullable = false, length = 6, unique = true)
    private String flightNumber;

    @Column(nullable = false)
    private OffsetDateTime estDepartureTime;

    @Column(nullable = false)
    private OffsetDateTime estArrivalTime;

    @Column(nullable = false)
    private Integer availableSeats;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getAirlineName() { return airlineName; }
    public void setAirlineName(String airlineName) { this.airlineName = airlineName; }
    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public OffsetDateTime getEstDepartureTime() { return estDepartureTime; }
    public void setEstDepartureTime(OffsetDateTime estDepartureTime) { this.estDepartureTime = estDepartureTime; }
    public OffsetDateTime getEstArrivalTime() { return estArrivalTime; }
    public void setEstArrivalTime(OffsetDateTime estArrivalTime) { this.estArrivalTime = estArrivalTime; }
    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }
}
