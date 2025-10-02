package com.example.week07.repo;

import com.example.week07.domain.Booking;
import com.example.week07.domain.AppUser;
import com.example.week07.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    long countByFlight(Flight flight);

    @Query("select b from Booking b where b.customer = :user and ((b.flight.estDepartureTime <= :end and b.flight.estArrivalTime >= :start))")
    List<Booking> findOverlapping(@Param("user") AppUser user,
                                  @Param("start") OffsetDateTime start,
                                  @Param("end") OffsetDateTime end);
}
