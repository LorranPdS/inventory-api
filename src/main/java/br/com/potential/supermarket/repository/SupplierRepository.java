package br.com.potential.supermarket.repository;

import br.com.potential.supermarket.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SupplierRepository extends JpaRepository<SupplierEntity, UUID> {
}
