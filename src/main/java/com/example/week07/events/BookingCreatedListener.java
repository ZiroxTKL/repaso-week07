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
        String body = "Hi " + b.getCustomerFirstName() + " " + b.getCustomerLastName() + ",\n\n" +
                " \n\n" +
                " " + b.getFlightNumber() + " " + b.getFlight().getEstDepartureTime().atZoneSameInstant(ZoneOffset.UTC).format(ISO_Z) +
                " " + b.getFlight().getEstArrivalTime().atZoneSameInstant(ZoneOffset.UTC).format(ISO_Z) + ".\n\n" +
                " " + b.getBookingDate().atZoneSameInstant(ZoneOffset.UTC).format(ISO_Z) + ".\n\n" +
                " ";
        Path out = Path.of(" " + b.getId() + ".txt");
        try {
            Files.writeString(out, body);
        } catch (IOException ignored) {

        }
    }
}
