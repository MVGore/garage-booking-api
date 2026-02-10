package com.garage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvgore.garageapi.dto.ServiceDTO;
import com.mvgore.garageapi.entity.Garage;
import com.mvgore.garageapi.entity.User;
import com.mvgore.garageapi.repository.GarageRepository;
import com.mvgore.garageapi.repository.UserRepository;
import com.mvgore.garageapi.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = com.mvgore.garageapi.GarageApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ServiceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private User owner;
    private Garage garage;
    private String ownerToken;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        garageRepository.deleteAll();

        // Create and save the owner
        owner = new User();
        owner.setName("owner1");
        owner.setEmail("owner1@test.com");
        owner.setPassword("pass");
        owner.setRole(User.Role.GARAGE_OWNER);
        userRepository.save(owner);

        // Create and save the garage
        garage = new Garage();
        garage.setName("Test Garage");
        garage.setLocation("Test Location");
        garage.setOwner(owner);
        garageRepository.save(garage);

        // Generate JWT for the owner
        ownerToken = jwtUtil.generateToken(owner.getEmail(), owner.getRole().name());
    }

    @Test
    void testAddService() throws Exception {
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setName("Oil Change");
        serviceDTO.setPrice(499.0);
        serviceDTO.setDuration(30);
        serviceDTO.setGarageId(garage.getId());

        mockMvc.perform(
                post("/api/services")
                        .header("Authorization", "Bearer " + ownerToken) // Make sure you pass the JWT token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceDTO))
        ).andExpect(status().isOk()); // Expect 200 OK since the owner is authorized
    }

    @Test
    void testAddService_AsNonOwner() throws Exception {
        // Create a non-owner user (customer)
        User customer = new User();
        customer.setName("customer1");
        customer.setEmail("customer1@test.com");
        customer.setPassword("pass");
        customer.setRole(User.Role.CUSTOMER);
        userRepository.save(customer);

        String customerToken = jwtUtil.generateToken(customer.getEmail(), customer.getRole().name());

        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setName("Oil Change");
        serviceDTO.setPrice(499.0);
        serviceDTO.setDuration(30);
        serviceDTO.setGarageId(garage.getId());

        mockMvc.perform(
                post("/api/services")
                        .header("Authorization", "Bearer " + customerToken) // Use customer token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceDTO))
        ).andExpect(status().isForbidden()); // Expect 403 Forbidden as the customer is not allowed
    }
}
