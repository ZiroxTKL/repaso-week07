package com.example.week07.events;

import com.example.week07.domain.Booking;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class BookingCreatedListener {
    private static final DateTimeFormatter ISO_Z = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Async
    @EventListener
    public void onBookingCreated(BookingCreatedEvent event) {
        Booking b = event.getBooking();
        String body = "Hello " + b.getCustomerFirstName() + " " + b.getCustomerLastName() + ",\n\n" +
                "Your booking was successful! \n\n" +
                "The booking is for flight " + b.getFlightNumber() + " with departure date of " + b.getFlight().getEstDepartureTime().atZoneSameInstant(ZoneOffset.UTC).format(ISO_Z) +
                " and arrival date of " + b.getFlight().getEstArrivalTime().atZoneSameInstant(ZoneOffset.UTC).format(ISO_Z) + ".\n\n" +
                "The booking was registered at " + b.getBookingDate().atZoneSameInstant(ZoneOffset.UTC).format(ISO_Z) + ".\n\n" +
                "Bon Voyage!\nFly Away Travel\n";
        Path out = Path.of("flight_booking_email_" + b.getId() + ".txt");
        try {
            Files.writeString(out, body);
        } catch (IOException e) {

        }
    }
}
