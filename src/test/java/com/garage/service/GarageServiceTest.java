package com.garage.service;

import com.mvgore.garageapi.entity.Garage;
import com.mvgore.garageapi.entity.User;
import com.mvgore.garageapi.repository.GarageRepository;
import com.mvgore.garageapi.service.GarageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GarageServiceTest {

    @InjectMocks
    private GarageService garageService;

    @Mock
    private GarageRepository garageRepository;

    private Garage garage;
    private User owner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        owner = new User();
        owner.setId(1L);
        owner.setName("Owner1");
        owner.setEmail("owner1@example.com");
        owner.setRole(User.Role.GARAGE_OWNER);

        garage = new Garage();
        garage.setId(1L);
        garage.setName("Super Garage");
        garage.setLocation("Pune");
        garage.setOwner(owner);
    }

    @Test
    void testCreateGarage() {
        when(garageRepository.save(garage)).thenReturn(garage);
        Garage saved = garageService.createGarage(garage);
        assertEquals("Super Garage", saved.getName());
        verify(garageRepository, times(1)).save(garage);
    }

    @Test
    void testUpdateGarage() {
        garage.setName("Updated Garage");
        when(garageRepository.save(garage)).thenReturn(garage);
        Garage updated = garageService.updateGarage(garage);
        assertEquals("Updated Garage", updated.getName());
        verify(garageRepository, times(1)).save(garage);
    }

    @Test
    void testGetGarageById() {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        Optional<Garage> result = garageService.getGarageById(1L);
        assertTrue(result.isPresent());
        assertEquals("Super Garage", result.get().getName());
    }

    @Test
    void testGetAllGarages() {
        when(garageRepository.findAll()).thenReturn(List.of(garage));
        List<Garage> garages = garageService.getAllGarages();
        assertEquals(1, garages.size());
    }

    @Test
    void testFilterGaragesByLocation() {
        when(garageRepository.findByLocationContaining("Pune")).thenReturn(List.of(garage));
        List<Garage> result = garageService.filterGarages("Pune", null);
        assertEquals(1, result.size());
    }

    @Test
    void testFilterGaragesByService() {
        when(garageRepository.findByServices_NameContaining("Oil Change")).thenReturn(List.of(garage));
        List<Garage> result = garageService.filterGarages(null, "Oil Change");
        assertEquals(1, result.size());
    }

    @Test
    void testFilterGaragesByLocationAndService() {
        when(garageRepository.findByLocationContainingAndServices_NameContaining("Pune", "Oil Change"))
                .thenReturn(List.of(garage));
        List<Garage> result = garageService.filterGarages("Pune", "Oil Change");
        assertEquals(1, result.size());
    }
}
