package com.example.week07.controller;

import com.example.week07.service.BookingService;
import com.example.week07.service.FlightService;
import com.example.week07.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/cleanup")
@RequestMapping("/cleanup")
public class CleanupController {
    private final UserService userService;
    private final FlightService flightService;
    private final BookingService bookingService;

    public CleanupController(UserService userService, FlightService flightService, BookingService bookingService) {
        this.userService = userService;
        this.flightService = flightService;
        this.bookingService = bookingService;
    }

    @DeleteMapping
    public ResponseEntity<Void> cleanup(){
        bookingService.deleteAll();
        flightService.deleteAll();
        userService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
