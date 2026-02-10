package com.garage.service;

import com.mvgore.garageapi.GarageApplication;
import com.mvgore.garageapi.entity.*;
import com.mvgore.garageapi.repository.*;
import com.mvgore.garageapi.service.BookingService;
import com.mvgore.garageapi.exception.BookingConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = GarageApplication.class)
@ActiveProfiles("test")
public class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    private User customer;
    private Garage garage;
    private GarageServiceEntity service;

    @BeforeEach
    void setup() {
        bookingRepository.deleteAll();
        serviceRepository.deleteAll();
        garageRepository.deleteAll();
        userRepository.deleteAll();

        customer = new User();
        customer.setName("Customer");
        customer.setEmail("customer@test.com");
        customer.setPassword("pass");
        customer.setRole(User.Role.CUSTOMER);
        userRepository.save(customer);

        User owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@test.com");
        owner.setPassword("pass");
        owner.setRole(User.Role.GARAGE_OWNER);
        userRepository.save(owner);

        garage = new Garage();
        garage.setName("Garage");
        garage.setLocation("Pune");
        garage.setOwner(owner);
        garageRepository.save(garage);

        service = new GarageServiceEntity();
        service.setGarage(garage);
        service.setName("Oil Change");
        service.setPrice(500.0);
        service.setDuration(30);
        serviceRepository.save(service);
    }

    @Test
    void testCreateBookingSuccess() {
        LocalDateTime bookingTime = LocalDateTime.of(2026, 2, 10, 10, 0); // fixed datetime

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setGarage(garage);
        booking.setService(service);
        booking.setBookingTime(bookingTime);

        Booking saved = bookingService.createBooking(booking);

        assertNotNull(saved.getId());
        assertEquals(BookingStatus.PENDING, saved.getStatus());
    }

    @Test
    void testBookingConflict() {
        LocalDateTime bookingTime = LocalDateTime.of(2026, 2, 10, 10, 0); // same fixed datetime

        Booking booking1 = new Booking();
        booking1.setCustomer(customer);
        booking1.setGarage(garage);
        booking1.setService(service);
        booking1.setBookingTime(bookingTime);
        bookingService.createBooking(booking1);

        Booking booking2 = new Booking();
        booking2.setCustomer(customer);
        booking2.setGarage(garage);
        booking2.setService(service);
        booking2.setBookingTime(bookingTime);

        assertThrows(BookingConflictException.class, () -> bookingService.createBooking(booking2));
    }
}
