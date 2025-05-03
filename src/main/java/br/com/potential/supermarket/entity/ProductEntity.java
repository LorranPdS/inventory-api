package br.com.potential.supermarket.entity;

import br.com.potential.supermarket.dto.request.ProductRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PRODUCT")
public class ProductEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "quantity_available", nullable = false)
    private Double quantityAvailable;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "fk_supplier", nullable = false)
    private SupplierEntity supplierEntity;

    @ManyToOne
    @JoinColumn(name = "fk_category", nullable = false)
    private CategoryEntity categoryEntity;

    public static ProductEntity of(ProductRequest request, SupplierEntity supplierEntity,
            CategoryEntity categoryEntity){

        return ProductEntity.builder()
                .name(request.getName())
                .quantityAvailable(request.getQuantityAvailable())
                .supplierEntity(supplierEntity)
                .categoryEntity(categoryEntity)
                .build();
    }

}
