package br.com.potential.supermarket.dto.response;

import br.com.potential.supermarket.entity.CategoryEntity;
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
