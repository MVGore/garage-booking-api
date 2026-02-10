package com.mvgore.garageapi.repository;

import com.mvgore.garageapi.entity.Booking;
import com.mvgore.garageapi.entity.Garage;
import com.mvgore.garageapi.entity.User;
import com.mvgore.garageapi.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomer(User customer);

    List<Booking> findByGarage(Garage garage);

    List<Booking> findByStatus(BookingStatus status);

    // Minimal change: only check overlapping bookingTime ranges
    @Query("SELECT b FROM Booking b WHERE b.garage = :garage AND b.bookingTime < :end AND b.bookingTime >= :start")
    List<Booking> findConflictingBookings(
            @Param("garage") Garage garage,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
