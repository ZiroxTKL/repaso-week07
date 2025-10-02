package com.example.week07.events;

import com.example.week07.domain.Booking;
import org.springframework.context.ApplicationEvent;

public class BookingCreatedEvent extends ApplicationEvent {
    private final Booking booking;
    public BookingCreatedEvent(Object source, Booking booking) {
        super(source);
        this.booking = booking;
    }
    public Booking getBooking() { return booking; }
}
