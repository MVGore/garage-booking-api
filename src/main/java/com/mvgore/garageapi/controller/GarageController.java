package com.mvgore.garageapi.controller;

import com.mvgore.garageapi.dto.GarageDTO;
import com.mvgore.garageapi.entity.Garage;
import com.mvgore.garageapi.entity.User;
import com.mvgore.garageapi.security.UserPrincipal;
import com.mvgore.garageapi.service.GarageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/garages")
public class GarageController {

    private final GarageService garageService;

    public GarageController(GarageService garageService) {
        this.garageService = garageService;
    }

    // GET with optional filtering by location and serviceType
    @GetMapping
    public ResponseEntity<List<GarageDTO>> getGarages(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String serviceType) {

        List<GarageDTO> garages = garageService
                .filterGarages(location, serviceType)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(garages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GarageDTO> getGarage(@PathVariable Long id) {
        Garage garage = garageService.getGarageById(id)
                .orElseThrow(() -> new RuntimeException("Garage not found"));
        return ResponseEntity.ok(mapToDTO(garage));
    }

    // Only GARAGE_OWNER can create a garage
    @PostMapping
    public ResponseEntity<GarageDTO> createGarage(@RequestBody GarageDTO garageDTO,
                                                  Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();

        if (user.getRole() != User.Role.GARAGE_OWNER) {
            return ResponseEntity.status(403).build();
        }

        Garage garage = mapFromDTO(garageDTO);
        garage.setOwner(user); // assign garage owner
        Garage saved = garageService.createGarage(garage);

        return ResponseEntity.ok(mapToDTO(saved));
    }

    // Only GARAGE_OWNER can update their own garage
    @PutMapping("/{id}")
    public ResponseEntity<GarageDTO> updateGarage(@PathVariable Long id,
                                                  @RequestBody GarageDTO garageDTO,
                                                  Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();

        Garage garage = garageService.getGarageById(id)
                .orElseThrow(() -> new RuntimeException("Garage not found"));

        if (user.getRole() != User.Role.GARAGE_OWNER || !garage.getOwner().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        // update fields from DTO
        garage.setName(garageDTO.getName());
        garage.setDescription(garageDTO.getDescription());
        garage.setLocation(garageDTO.getLocation());
        garage.setPriceRange(garageDTO.getPriceRange());

        Garage updated = garageService.updateGarage(garage);
        return ResponseEntity.ok(mapToDTO(updated));
    }

    // DTO mapping helpers
    private GarageDTO mapToDTO(Garage garage) {
        GarageDTO dto = new GarageDTO();
        dto.setId(garage.getId());
        dto.setName(garage.getName());
        dto.setDescription(garage.getDescription());
        dto.setLocation(garage.getLocation());
        dto.setPriceRange(garage.getPriceRange());
        return dto;
    }

    private Garage mapFromDTO(GarageDTO dto) {
        Garage garage = new Garage();
        garage.setName(dto.getName());
        garage.setDescription(dto.getDescription());
        garage.setLocation(dto.getLocation());
        garage.setPriceRange(dto.getPriceRange());
        return garage;
    }
}
