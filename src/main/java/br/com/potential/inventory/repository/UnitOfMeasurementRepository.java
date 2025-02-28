package br.com.potential.inventory.repository;

import br.com.potential.inventory.entity.UnitOfMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UnitOfMeasurementRepository extends JpaRepository<UnitOfMeasurementEntity, UUID> {
}
