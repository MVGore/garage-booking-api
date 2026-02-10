package com.garage.controller;

import com.mvgore.garageapi.entity.*;
import com.mvgore.garageapi.repository.*;
import com.mvgore.garageapi.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = com.mvgore.garageapi.GarageApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BookingControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private GarageRepository garageRepository;
    @Autowired private ServiceRepository serviceRepository;
    @Autowired private JwtUtil jwtUtil;

    private Booking booking;
    private String ownerToken;
    private String customerToken;

    @BeforeEach
    void setup() {
        bookingRepository.deleteAll();
        serviceRepository.deleteAll();
        garageRepository.deleteAll();
        userRepository.deleteAll();

        User owner = new User();
        owner.setName("owner1");
        owner.setEmail("owner1@test.com");
        owner.setPassword("pass");
        owner.setRole(User.Role.GARAGE_OWNER);
        userRepository.save(owner);

        User customer = new User();
        customer.setName("customer1");
        customer.setEmail("customer1@test.com");
        customer.setPassword("pass");
        customer.setRole(User.Role.CUSTOMER);
        userRepository.save(customer);

        Garage garage = new Garage();
        garage.setName("Test Garage");
        garage.setLocation("Test Location");
        garage.setOwner(owner);
        garageRepository.save(garage);

        GarageServiceEntity service = new GarageServiceEntity();
        service.setName("Oil Change");
        service.setPrice(499.0);
        service.setDuration(30);
        service.setGarage(garage);
        serviceRepository.save(service);

        booking = new Booking();
        booking.setGarage(garage);
        booking.setCustomer(customer);
        booking.setService(service);
        booking.setBookingTime(LocalDateTime.now().plusDays(1));
        booking.setStatus(BookingStatus.PENDING);
        bookingRepository.save(booking);

        ownerToken = jwtUtil.generateToken(owner.getEmail(), owner.getRole().name());
        customerToken = jwtUtil.generateToken(customer.getEmail(), customer.getRole().name());
    }

    @Test
    void testUpdateBookingStatus_asOwner() throws Exception {
        mockMvc.perform(
                put("/api/bookings/" + booking.getId() + "/status?status=ACCEPTED") // Pass status as query parameter
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void testUpdateBookingStatus_asCustomerCancel() throws Exception {
        mockMvc.perform(
                put("/api/bookings/" + booking.getId() + "/status?status=CANCELLED") // Pass status as query parameter
                        .header("Authorization", "Bearer " + customerToken)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

}
