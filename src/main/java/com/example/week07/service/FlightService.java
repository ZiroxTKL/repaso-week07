package com.example.week07.service;

import com.example.week07.domain.Flight;
import com.example.week07.dto.Dtos.NewFlightManyRequestDTO;
import com.example.week07.dto.Dtos.NewFlightRequestDTO;
import com.example.week07.repo.FlightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FlightService {
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) { this.flightRepository = flightRepository; }

    private static boolean isValidFlightNumber(String fn) {
        return fn != null && fn.matches("^[A-Z0-9]{1,6}$");
    }

    @Transactional
    public UUID create(NewFlightRequestDTO dto) {
        if (dto.airlineName == null || dto.airlineName.isBlank() ||
                !isValidFlightNumber(dto.flightNumber) ||
                dto.estDepartureTime == null || dto.estArrivalTime == null ||
                dto.availableSeats == null || dto.availableSeats <= 0) {
            throw new IllegalArgumentException("Invalid flight data");
        }
        if (!dto.estDepartureTime.isBefore(dto.estArrivalTime)) throw new IllegalArgumentException("Invalid times");
        flightRepository.findByFlightNumberIgnoreCase(dto.flightNumber).ifPresent(f -> { throw new IllegalArgumentException("Flight number exists"); });
        Flight f = new Flight();
        f.setAirlineName(dto.airlineName);
        f.setFlightNumber(dto.flightNumber.toUpperCase());
        f.setEstDepartureTime(dto.estDepartureTime);
        f.setEstArrivalTime(dto.estArrivalTime);
        f.setAvailableSeats(dto.availableSeats);
        f = flightRepository.save(f);
        return f.getId();
    }

    @Transactional
    public int createMany(NewFlightManyRequestDTO req) {
        if (req == null || req.flights == null) return 0;
        int created = 0;
        for (NewFlightRequestDTO dto : req.flights) {
            try { create(dto); created++; } catch (Exception ignored) {}
        }
        return created;
    }

    @Transactional(readOnly = true)
    public List<Flight> search(String flightNumber, String airlineName, OffsetDateTime from, OffsetDateTime to) {
        return flightRepository.search(emptyToNull(flightNumber), emptyToNull(airlineName), from, to);
    }

    private static String emptyToNull(String s){ return (s==null || s.isBlank())? null : s; }

    public Flight getById(UUID id) { return flightRepository.findById(id).orElseThrow(); }
    public void deleteAll(){ flightRepository.deleteAll(); }
}
