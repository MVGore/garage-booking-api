package com.garage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvgore.garageapi.entity.*;
import com.mvgore.garageapi.repository.*;
import com.mvgore.garageapi.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = com.mvgore.garageapi.GarageApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class GarageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private User owner;
    private String ownerToken;

    @BeforeEach
    void setup() {
        garageRepository.deleteAll();
        userRepository.deleteAll();

        owner = new User();
        owner.setName("Garage Owner");
        owner.setEmail("owner@test.com");
        owner.setPassword("pass");
        owner.setRole(User.Role.GARAGE_OWNER);
        userRepository.save(owner);

        // Generate JWT for owner
        ownerToken = jwtUtil.generateToken(owner.getEmail(), owner.getRole().name());
    }

    @Test
    void testCreateGarage() throws Exception {
        Garage garage = new Garage();
        garage.setName("New Garage");
        garage.setLocation("Mumbai");
        garage.setOwner(owner);

        mockMvc.perform(post("/api/garages")
                        .header("Authorization", "Bearer " + ownerToken) // JWT header added
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(garage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Garage"));
    }

    @Test
    void testGetGarages() throws Exception {
        mockMvc.perform(get("/api/garages")
                        .header("Authorization", "Bearer " + ownerToken)) // JWT header added
                .andExpect(status().isOk());
    }
}
