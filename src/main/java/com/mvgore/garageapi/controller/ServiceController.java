package com.mvgore.garageapi.controller;

import com.mvgore.garageapi.dto.ServiceDTO;
import com.mvgore.garageapi.entity.Garage;
import com.mvgore.garageapi.entity.GarageServiceEntity;
import com.mvgore.garageapi.entity.User;
import com.mvgore.garageapi.security.UserPrincipal;
import com.mvgore.garageapi.service.GarageService;
import com.mvgore.garageapi.service.ServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;
    private final GarageService garageService; // ✅ REQUIRED

    public ServiceController(ServiceService serviceService,
                             GarageService garageService) {
        this.serviceService = serviceService;
        this.garageService = garageService;
    }

    /* =========================
       GET SERVICES BY GARAGE
       ========================= */
    @GetMapping("/garage/{garageId}")
    public ResponseEntity<List<ServiceDTO>> getServicesByGarage(
            @PathVariable Long garageId) {

        List<ServiceDTO> services = serviceService.getServicesByGarage(garageId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(services);
    }

    /* =========================
       ADD SERVICE
       ========================= */
    @PostMapping
    public ResponseEntity<ServiceDTO> addService(
            @RequestBody ServiceDTO serviceDTO,
            Authentication authentication) {

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        if (user.getRole() != User.Role.GARAGE_OWNER) {
            return ResponseEntity.status(403).build();
        }

        // ✅ CORRECT SERVICE USED
        Garage garage = garageService.getGarageById(serviceDTO.getGarageId())
                .orElseThrow(() -> new RuntimeException("Garage not found"));

        GarageServiceEntity service = new GarageServiceEntity();
        service.setName(serviceDTO.getName());
        service.setPrice(serviceDTO.getPrice());
        service.setDuration(serviceDTO.getDuration());
        service.setGarage(garage); // 🔥 THIS WAS MISSING BEFORE

        GarageServiceEntity saved = serviceService.addService(service);

        return ResponseEntity.ok(mapToDTO(saved));
    }

    /* =========================
       UPDATE SERVICE
       ========================= */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceDTO> updateService(
            @PathVariable Long id,
            @RequestBody ServiceDTO serviceDTO,
            Authentication authentication) {

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        if (user.getRole() != User.Role.GARAGE_OWNER) {
            return ResponseEntity.status(403).build();
        }

        GarageServiceEntity service = serviceService.getServiceById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        service.setName(serviceDTO.getName());
        service.setPrice(serviceDTO.getPrice());
        service.setDuration(serviceDTO.getDuration());

        GarageServiceEntity updated = serviceService.updateService(service);
        return ResponseEntity.ok(mapToDTO(updated));
    }

    /* =========================
       MAPPERS
       ========================= */
    private ServiceDTO mapToDTO(GarageServiceEntity service) {
        ServiceDTO dto = new ServiceDTO();
        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setPrice(service.getPrice());
        dto.setDuration(service.getDuration());
        dto.setGarageId(service.getGarage().getId());
        return dto;
    }
}
