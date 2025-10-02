package com.example.week07.controller;

import com.example.week07.dto.Dtos.BookingInfoDTO;
import com.example.week07.dto.Dtos.FlightBookRequestDTO;
import com.example.week07.dto.Dtos.NewIdDTO;
import com.example.week07.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class BookingController {
    private final BookingService bookingService;
    public BookingController(BookingService bookingService) { this.bookingService = bookingService; }

    @PostMapping("/flights/book")
    public ResponseEntity<NewIdDTO> book(@RequestBody FlightBookRequestDTO req){
        UUID id = bookingService.book(req);
        return ResponseEntity.ok(new NewIdDTO(id));
    }

    @GetMapping("/flight/book/{id}")
    public ResponseEntity<BookingInfoDTO> getBooking(@PathVariable("id") UUID id){
        return ResponseEntity.ok(bookingService.getBookingInfo(id));
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<BookingInfoDTO> getBookingAlt(@PathVariable("id") UUID id){
        return ResponseEntity.ok(bookingService.getBookingInfo(id));
    }
}
