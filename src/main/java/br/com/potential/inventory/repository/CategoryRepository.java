package br.com.potential.inventory.repository;

import br.com.potential.inventory.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    List<CategoryEntity> findByDescriptionIgnoreCaseContaining(String description);

    Optional<CategoryEntity> findByCode(String code);
}
