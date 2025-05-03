package br.com.potential.supermarket.dto.request;

import br.com.potential.supermarket.exception.ExceptionMessages;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupplierRequest {

    @NotBlank(message = ExceptionMessages.SUPPLIER_NAME_MUST_BE_INFORMED)
    private String name;
}
