package br.com.potential.supermarket.dto.request;

import br.com.potential.supermarket.exception.ExceptionMessages;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank(message = ExceptionMessages.CATEGORY_CODE_MUST_BE_INFORMED)
    private String code;

    @NotBlank(message = ExceptionMessages.CATEGORY_DESCRIPTION_MUST_BE_INFORMED)
    private String description;

}
