package br.com.potential.inventory.dto.response;

import br.com.potential.inventory.entity.CategoryEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

@Data
public class CategoryResponse {

    private UUID id;
    private String code;
    private String description;

    public static CategoryResponse of(CategoryEntity categoryEntity){
        var categoryResponse = new CategoryResponse();
        BeanUtils.copyProperties(categoryEntity, categoryResponse);
        return categoryResponse;
    }
}
