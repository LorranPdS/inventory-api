package br.com.potential.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
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

    @ManyToOne
    @JoinColumn(name = "FK_SUPPLIER", nullable = false)
    private SupplierEntity supplierEntity;

    @ManyToOne
    @JoinColumn(name = "FK_CATEGORY", nullable = false)
    private CategoryEntity categoryEntity;

}
