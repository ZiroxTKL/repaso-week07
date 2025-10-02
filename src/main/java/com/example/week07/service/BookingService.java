package com.example.week07.service;

import com.example.week07.domain.AppUser;
import com.example.week07.domain.Booking;
import com.example.week07.domain.Flight;
import com.example.week07.dto.Dtos.BookingInfoDTO;
import com.example.week07.dto.Dtos.FlightBookRequestDTO;
import com.example.week07.events.BookingCreatedEvent;
import com.example.week07.repo.BookingRepository;
import com.example.week07.repo.FlightRepository;
import com.example.week07.repo.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher events;

    public BookingService(BookingRepository bookingRepository, FlightRepository flightRepository, UserRepository userRepository, ApplicationEventPublisher events) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
        this.events = events;
    }

    private UUID currentUserId() { 
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) throw new IllegalStateException("Unauthenticated");
        return UUID.fromString(auth.getPrincipal().toString());
    }

    @Transactional
    public UUID book(FlightBookRequestDTO req) {
        if (req == null || req.flightId == null) throw new IllegalArgumentException("Flight Id required");
        UUID userId = currentUserId();
        AppUser user = userRepository.findById(userId).orElseThrow();
        Flight flight = flightRepository.findWithLockingById(req.flightId).orElseThrow();

        // Optional: do not allow past or in-transit
        OffsetDateTime now = OffsetDateTime.now();
        if (!now.isBefore(flight.getEstDepartureTime())) {
            throw new IllegalArgumentException("Flight not available for booking");
        }

        // Optional: overlaps
        if (!bookingRepository.findOverlapping(user, flight.getEstDepartureTime(), flight.getEstArrivalTime()).isEmpty()) {
            throw new IllegalArgumentException("Overlapping booking");
        }

        long sold = bookingRepository.countByFlight(flight);
        if (sold >= flight.getAvailableSeats()) {
            throw new IllegalStateException("No seats available");
        }

        Booking b = new Booking();
        b.setFlight(flight);
        b.setCustomer(user);
        b.setBookingDate(OffsetDateTime.now());
        b.setFlightNumber(flight.getFlightNumber());
        b.setCustomerFirstName(user.getFirstName());
        b.setCustomerLastName(user.getLastName());
        b = bookingRepository.save(b);

        events.publishEvent(new BookingCreatedEvent(this, b));

        return b.getId();
    }

    public BookingInfoDTO getBookingInfo(UUID id) {
        Booking b = bookingRepository.findById(id).orElseThrow();
        BookingInfoDTO dto = new BookingInfoDTO();
        dto.id = b.getId();
        dto.bookingDate = b.getBookingDate();
        dto.flightId = b.getFlight().getId();
        dto.flightNumber = b.getFlightNumber();
        dto.customerId = b.getCustomer().getId();
        dto.customerFirstName = b.getCustomerFirstName();
        dto.customerLastName = b.getCustomerLastName();
        return dto;
    }

    public void deleteAll(){ bookingRepository.deleteAll(); }
}
