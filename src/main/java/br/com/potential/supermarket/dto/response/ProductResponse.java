package br.com.potential.supermarket.dto.response;

import br.com.potential.supermarket.entity.ProductEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private UUID id;
    private String name;
    @JsonProperty("quantity_available")
    private Double quantityAvailable;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    private SupplierResponse supplier;
    private CategoryResponse category;

    public static ProductResponse of(ProductEntity productEntity){
        return ProductResponse.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .quantityAvailable(productEntity.getQuantityAvailable())
                .createdAt(productEntity.getCreatedAt())
                .supplier(SupplierResponse.of(productEntity.getSupplierEntity()))
                .category(CategoryResponse.of(productEntity.getCategoryEntity()))
                .build();
    }
}
