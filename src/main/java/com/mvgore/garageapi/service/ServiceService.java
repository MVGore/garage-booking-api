package com.mvgore.garageapi.service;

import com.mvgore.garageapi.entity.GarageServiceEntity;
import com.mvgore.garageapi.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository){
        this.serviceRepository = serviceRepository;
    }

    public GarageServiceEntity addService(GarageServiceEntity service){
        return serviceRepository.save(service);
    }

    public GarageServiceEntity updateService(GarageServiceEntity service){
        return serviceRepository.save(service);
    }

    public Optional<GarageServiceEntity> getServiceById(Long id){
        return serviceRepository.findById(id);
    }

    public List<GarageServiceEntity> getServicesByGarage(Long garageId){
        return serviceRepository.findByGarageId(garageId);
    }
}
