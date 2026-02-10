package com.mvgore.garageapi.controller;

import com.mvgore.garageapi.dto.BookingDTO;
import com.mvgore.garageapi.dto.BookingRequest;
import com.mvgore.garageapi.dto.BookingResponse;
import com.mvgore.garageapi.entity.*;
import com.mvgore.garageapi.exception.*;
import com.mvgore.garageapi.security.UserPrincipal;
import com.mvgore.garageapi.service.BookingService;
import com.mvgore.garageapi.service.GarageService;
import com.mvgore.garageapi.service.ServiceService;
import com.mvgore.garageapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final GarageService garageService;
    private final ServiceService serviceService;

    public BookingController(
            BookingService bookingService,
            UserService userService,
            GarageService garageService,
            ServiceService serviceService
    ) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.garageService = garageService;
        this.serviceService = serviceService;
    }

    /* =========================
       CREATE BOOKING
       ========================= */
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @RequestBody BookingRequest request
    ) {

        User customer = userService.findByEmail(request.getCustomerEmail())
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        Garage garage = garageService.getGarageById(request.getGarageId())
                .orElseThrow(() ->
                        new GarageNotFoundException("Garage not found"));

        GarageServiceEntity service =
                serviceService.getServiceById(request.getServiceId())
                        .orElseThrow(() ->
                                new ServiceNotFoundException("Service not found"));

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setGarage(garage);
        booking.setService(service);
        booking.setBookingTime(request.getBookingTime());

        Booking saved = bookingService.createBooking(booking);

        BookingResponse response = new BookingResponse();
        response.setBookingId(saved.getId());
        response.setStatus(saved.getStatus());

        return ResponseEntity.ok(response);
    }

    /* =========================
       GET BOOKINGS
       ========================= */
    @GetMapping("/customer/{email}")
    public ResponseEntity<List<BookingDTO>> getBookingsByCustomer(
            @PathVariable String email
    ) {

        User customer = userService.findByEmail(email)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        return ResponseEntity.ok(
                bookingService.getBookingsByCustomer(customer)
                        .stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/garage/{id}")
    public ResponseEntity<List<BookingDTO>> getBookingsByGarage(
            @PathVariable Long id
    ) {

        Garage garage = garageService.getGarageById(id)
                .orElseThrow(() ->
                        new GarageNotFoundException("Garage not found"));

        return ResponseEntity.ok(
                bookingService.getBookingsByGarage(garage)
                        .stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList())
        );
    }

    /* =========================
       UPDATE BOOKING STATUS
       ========================= */
    @PutMapping("/{id}/status")
    public ResponseEntity<BookingDTO> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status,
            Authentication authentication
    ) {

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();
        User actingUser = principal.getUser();

        Booking booking = bookingService.getBookingById(id)
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found"));

        Booking updated =
                bookingService.updateBookingStatus(
                        booking, status, actingUser);

        return ResponseEntity.ok(mapToDTO(updated));
    }

    /* =========================
       MAPPER
       ========================= */
    private BookingDTO mapToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setCustomerEmail(booking.getCustomer().getEmail());
        dto.setGarageId(booking.getGarage().getId());
        dto.setServiceId(booking.getService().getId());
        dto.setBookingTime(booking.getBookingTime());
        dto.setStatus(booking.getStatus());
        return dto;
    }
}
