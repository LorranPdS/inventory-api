package br.com.potential.inventory.entity;

import br.com.potential.inventory.dto.request.SupplierRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SUPPLIER")
public class SupplierEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    public static SupplierEntity of(SupplierRequest supplierRequest){
        var supplierEntity = new SupplierEntity();
        BeanUtils.copyProperties(supplierRequest, supplierEntity);
        return supplierEntity;
    }
}
