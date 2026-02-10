package com.mvgore.garageapi.service;

import com.mvgore.garageapi.entity.Garage;
import com.mvgore.garageapi.repository.GarageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GarageService {

    private final GarageRepository garageRepository;

    public GarageService(GarageRepository garageRepository){
        this.garageRepository = garageRepository;
    }

    public Garage createGarage(Garage garage){
        return garageRepository.save(garage);
    }

    public Garage updateGarage(Garage garage){
        return garageRepository.save(garage);
    }

    public Optional<Garage> getGarageById(Long id){
        return garageRepository.findById(id);
    }

    public List<Garage> getAllGarages(){
        return garageRepository.findAll();
    }

    public List<Garage> filterGarages(String location, String serviceType){
        if(location != null && serviceType != null){
            return garageRepository.findByLocationContainingAndServices_NameContaining(location, serviceType);
        } else if(location != null){
            return garageRepository.findByLocationContaining(location);
        } else if(serviceType != null){
            return garageRepository.findByServices_NameContaining(serviceType);
        } else {
            return garageRepository.findAll();
        }
    }
}
