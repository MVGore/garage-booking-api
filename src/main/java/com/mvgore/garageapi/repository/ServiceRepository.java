package com.mvgore.garageapi.repository;

import com.mvgore.garageapi.entity.GarageServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceRepository extends JpaRepository<GarageServiceEntity, Long> {
    List<GarageServiceEntity> findByGarageId(Long garageId);
}
