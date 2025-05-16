package br.com.potential.supermarket.service;

import br.com.potential.supermarket.dto.PageResponseDto;
import br.com.potential.supermarket.dto.request.CategoryRequest;
import br.com.potential.supermarket.dto.response.CategoryResponse;
import br.com.potential.supermarket.dto.response.SuccessResponse;
import br.com.potential.supermarket.entity.CategoryEntity;
import br.com.potential.supermarket.exception.ExceptionMessages;
import br.com.potential.supermarket.exception.ValidationException;
import br.com.potential.supermarket.interfaces.CategoryService;
import br.com.potential.supermarket.interfaces.ProductService;
import br.com.potential.supermarket.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORY_DELETED = "Category was deleted.";
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

    @Override
    public CategoryResponse save(CategoryRequest categoryRequest){
        var categoryEntity = categoryRepository.save(CategoryEntity.of(categoryRequest));
        return CategoryResponse.of(categoryEntity);
    }

    @Override
    public CategoryResponse update(CategoryRequest categoryRequest, UUID categoryId) {
        validateInformedId(categoryId);
        var categoryEntity = CategoryEntity.of(categoryRequest);
        categoryEntity.setId(categoryId);
        categoryEntity = categoryRepository.save(categoryEntity);
        return CategoryResponse.of(categoryEntity);
    }

    @Override
    public CategoryResponse findById(UUID id){
        return CategoryResponse.of(this.findByIdEntity(id));
    }

    @Override
    public CategoryEntity findByIdEntity(UUID id){
        validateInformedId(id);
        return categoryRepository.findById(id).get();
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

    @Override
    public SuccessResponse delete(UUID categoryId){
        validateInformedId(categoryId);
        if(productService.existsByCategoryId(categoryId)){
            throw new ValidationException(ExceptionMessages.CATEGORY_DEFINED_IN_PRODUCT);
        }
        categoryRepository.deleteById(categoryId);
        return SuccessResponse.create(CATEGORY_DELETED);
    }

    private void validateInformedId(UUID id) {
        if(isEmpty(id)){
            throw new ValidationException(ExceptionMessages.CATEGORY_ID_MUST_BE_INFORMED);
        }
        if(categoryRepository.findById(id).isEmpty()){
            throw new ValidationException(ExceptionMessages.CATEGORY_ID_NOT_FOUND);
        }
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
