package br.com.potential.inventory.service;

import br.com.potential.inventory.interfaces.CategoryService;
import br.com.potential.inventory.dto.CategoryRequest;
import br.com.potential.inventory.dto.CategoryResponse;
import br.com.potential.inventory.entity.CategoryEntity;
import br.com.potential.inventory.exception.ExceptionMessages;
import br.com.potential.inventory.exception.ValidationException;
import br.com.potential.inventory.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryResponse save(CategoryRequest categoryRequest){
        validateCategoryNameInformed(categoryRequest);
        var categoryEntity = categoryRepository.save(CategoryEntity.of(categoryRequest));
        return CategoryResponse.of(categoryEntity);
    }

    private void validateCategoryNameInformed(CategoryRequest categoryRequest){
        if(isEmpty(categoryRequest.getDescription())){
            throw new ValidationException(ExceptionMessages.CATEGORY_DESCRIPTION_NOT_INFORMED);
        }
    }
}
