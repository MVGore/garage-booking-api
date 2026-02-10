package com.mvgore.garageapi.service;

import com.mvgore.garageapi.entity.*;
import com.mvgore.garageapi.exception.BookingConflictException;
import com.mvgore.garageapi.exception.ForbiddenOperationException;
import com.mvgore.garageapi.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /* =========================
       CREATE BOOKING
       ========================= */
    public Booking createBooking(Booking booking) {
        validateBookingTime(booking);
        booking.setStatus(BookingStatus.PENDING);
        return bookingRepository.save(booking);
    }

    /* =========================
       UPDATE BOOKING STATUS
       ========================= */
    public Booking updateBookingStatus(
            Booking booking,
            BookingStatus newStatus,
            User actingUser
    ) {

        if (actingUser == null || actingUser.getRole() == null) {
            throw new ForbiddenOperationException("Unauthenticated user");
        }

        User.Role role = actingUser.getRole();

        // 🚫 CUSTOMER: NOT ALLOWED TO UPDATE STATUS AT ALL
            if (role == User.Role.CUSTOMER) {
                throw new ForbiddenOperationException(
                        "Customers are not allowed to update booking status"
                );
            }

        // -------- GARAGE OWNER RULES --------
        else if (role == User.Role.GARAGE_OWNER) {

            if (booking.getGarage() == null
                    || booking.getGarage().getOwner() == null
                    || !booking.getGarage().getOwner().getId().equals(actingUser.getId())) {
                throw new ForbiddenOperationException(
                        "Garage owner cannot modify bookings of another garage"
                );
            }
        }

        // -------- ANY OTHER ROLE --------
        else {
            throw new ForbiddenOperationException("Unauthorized role");
        }

        booking.setStatus(newStatus);
        return bookingRepository.save(booking);
    }

    /* =========================
       VALIDATION
       ========================= */
    private void validateBookingTime(Booking booking) {

        LocalDateTime start = booking.getBookingTime();
        int duration = booking.getService().getDuration();
        LocalDateTime end = start.plusMinutes(duration);

        List<Booking> conflicts =
                bookingRepository.findConflictingBookings(
                        booking.getGarage(), start, end
                );

        // exclude same booking during updates
        conflicts.removeIf(b ->
                booking.getId() != null && b.getId().equals(booking.getId())
        );

        if (!conflicts.isEmpty()) {
            throw new BookingConflictException(
                    "Selected time slot is already booked for this garage"
            );
        }
    }

    /* =========================
       READ
       ========================= */
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getBookingsByCustomer(User customer) {
        return bookingRepository.findByCustomer(customer);
    }

    public List<Booking> getBookingsByGarage(Garage garage) {
        return bookingRepository.findByGarage(garage);
    }

    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }
}
