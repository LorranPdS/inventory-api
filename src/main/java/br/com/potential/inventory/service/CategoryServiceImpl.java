package br.com.potential.inventory.service;

import br.com.potential.inventory.dto.CategoryRequest;
import br.com.potential.inventory.dto.CategoryResponse;
import br.com.potential.inventory.dto.PageResponseDto;
import br.com.potential.inventory.entity.CategoryEntity;
import br.com.potential.inventory.exception.ExceptionMessages;
import br.com.potential.inventory.exception.ValidationException;
import br.com.potential.inventory.interfaces.CategoryService;
import br.com.potential.inventory.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryResponse save(CategoryRequest categoryRequest){
        var categoryEntity = categoryRepository.save(CategoryEntity.of(categoryRequest));
        return CategoryResponse.of(categoryEntity);
    }

    @Override
    public CategoryResponse findById(UUID id){
        return CategoryResponse.of(findByIdEntity(id));
    }

    @Override
    public CategoryResponse findByCode(String code) {
        return CategoryResponse.of(findByCodeEntity(code));
    }

    @Override
    public List<CategoryResponse> findByDescription(String description) {
        return findByDescriptionEntity(description);
    }

    @Override
    public PageResponseDto<CategoryResponse> findAll(Pageable pagination) {
        var categoryPage = categoryRepository.findAll(pagination)
                .map(CategoryResponse::of);

        return new PageResponseDto<>(
                categoryPage.getContent(),
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages());
    }

    private CategoryEntity findByIdEntity(UUID id){
        if(isEmpty(id)){
            throw new ValidationException(ExceptionMessages.CATEGORY_ID_MUST_BE_INFORMED);
        }
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ValidationException(ExceptionMessages.CATEGORY_ID_NOT_FOUND));
    }

    private CategoryEntity findByCodeEntity(String code) {
        if(isEmpty(code)){
            throw new ValidationException(ExceptionMessages.CATEGORY_CODE_MUST_BE_INFORMED);
        }
        return categoryRepository.findByCode(code)
                .orElseThrow(() -> new ValidationException(ExceptionMessages.CATEGORY_CODE_NOT_FOUND));
    }

    private List<CategoryResponse> findByDescriptionEntity(String description) {
        if(isEmpty(description)){
            throw new ValidationException(ExceptionMessages.CATEGORY_DESCRIPTION_MUST_BE_INFORMED);
        }

        return categoryRepository.findByDescriptionIgnoreCaseContaining(description)
                .stream()
                .map(CategoryResponse::of)
                .collect(Collectors.toList());
    }
}
