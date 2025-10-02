package com.example.week07.controller;

import com.example.week07.domain.Flight;
import com.example.week07.dto.Dtos.*;
import com.example.week07.service.FlightService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/flights")
public class FlightController {
    private final FlightService flightService;
    public FlightController(FlightService flightService) { this.flightService = flightService; }

    @PostMapping("/create")
    public ResponseEntity<NewIdDTO> create(@RequestBody NewFlightRequestDTO dto){
        UUID id = flightService.create(dto);
        return ResponseEntity.ok(new NewIdDTO(id));
    }

    @PostMapping("/create-many")
    public ResponseEntity<NewFlightManyResponseDTO> createMany(@RequestBody NewFlightManyRequestDTO request){
        int count = flightService.createMany(request);
        NewFlightManyResponseDTO resp = new NewFlightManyResponseDTO();
        resp.created = count;
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/search")
    public ResponseEntity<FlightSearchResponseDTO> search(@RequestParam(required = false) String flightNumber,
                                                          @RequestParam(required = false) String airlineName,
                                                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime estDepartureTimeFrom,
                                                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime estDepartureTimeTo){
        List<Flight> flights = flightService.search(flightNumber, airlineName, estDepartureTimeFrom, estDepartureTimeTo);
        FlightSearchResponseDTO dto = new FlightSearchResponseDTO();
        dto.flights = flights.stream().map(f -> {
            FlightResponseDTO fr = new FlightResponseDTO();
            fr.id = f.getId();
            fr.airlineName = f.getAirlineName();
            fr.flightNumber = f.getFlightNumber();
            fr.estDepartureTime = f.getEstDepartureTime();
            fr.estArrivalTime = f.getEstArrivalTime();
            fr.availableSeats = f.getAvailableSeats();
            return fr;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> getById(@PathVariable("id") UUID id){
        Flight f = flightService.getById(id);
        FlightResponseDTO fr = new FlightResponseDTO();
        fr.id = f.getId();
        fr.airlineName = f.getAirlineName();
        fr.flightNumber = f.getFlightNumber();
        fr.estDepartureTime = f.getEstDepartureTime();
        fr.estArrivalTime = f.getEstArrivalTime();
        fr.availableSeats = f.getAvailableSeats();
        return ResponseEntity.ok(fr);
    }
}
