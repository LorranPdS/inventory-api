package br.com.potential.inventory.dto.request;

import br.com.potential.inventory.exception.ExceptionMessages;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductRequest {

    @NotBlank(message = ExceptionMessages.PRODUCT_NAME_MUST_BE_INFORMED)
    private String name;

    @NotNull(message = ExceptionMessages.QUANTITY_AVAILABLE_MUST_BE_INFORMED)
    @JsonProperty("quantity_available")
    private Double quantityAvailable;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @NotNull(message = ExceptionMessages.SUPPLIER_ID_MUST_BE_INFORMED)
    private UUID supplierId;

    @NotNull(message = ExceptionMessages.CATEGORY_ID_MUST_BE_INFORMED)
    private UUID categoryId;

}
