package com.example.week07.repo;

import com.example.week07.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlightRepository extends JpaRepository<Flight, UUID> {
    Optional<Flight> findByFlightNumberIgnoreCase(String flightNumber);

    @Query("select f from Flight f where (:fn is null or upper(f.flightNumber) like concat('%', upper(:fn), '%')) and (:an is null or upper(f.airlineName) like concat('%', upper(:an), '%')) and (:from is null or f.estDepartureTime >= :from) and (:to is null or f.estDepartureTime <= :to)")
    List<Flight> search(@Param("fn") String flightNumber,
                        @Param("an") String airlineName,
                        @Param("from") OffsetDateTime from,
                        @Param("to") OffsetDateTime to);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Flight> findWithLockingById(UUID id);
}
