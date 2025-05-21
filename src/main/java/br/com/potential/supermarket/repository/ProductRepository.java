package br.com.potential.supermarket.repository;

import br.com.potential.supermarket.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    List<ProductEntity> findByNameIgnoreCaseContaining(String name);

    List<ProductEntity> findByCategoryEntityId(UUID categoryId);

    List<ProductEntity> findBySupplierEntityId(UUID supplierId);

    Boolean existsBySupplierEntityId(UUID supplierId);

    Boolean existsByCategoryEntityId(UUID categoryId);

}
