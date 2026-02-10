package com.mvgore.garageapi.repository;

import com.mvgore.garageapi.entity.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GarageRepository extends JpaRepository<Garage, Long> {
    List<Garage> findByLocationContaining(String location);
    List<Garage> findByServices_NameContaining(String serviceName);
    List<Garage> findByLocationContainingAndServices_NameContaining(String location, String serviceName);
}
