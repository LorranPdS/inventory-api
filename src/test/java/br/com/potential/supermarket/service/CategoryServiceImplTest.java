package br.com.potential.supermarket.service;

import br.com.potential.supermarket.dto.request.CategoryRequest;
import br.com.potential.supermarket.dto.response.CategoryResponse;
import br.com.potential.supermarket.entity.CategoryEntity;
import br.com.potential.supermarket.exception.ValidationException;
import br.com.potential.supermarket.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.potential.supermarket.exception.ExceptionMessages.*;
import static java.util.Objects.isNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Should save category when all fields are filled")
    void shouldSaveCategory_whenAllFieldsAreFilled() {
        var categoryRequest = getCategoryRequest();
        mockCategorySavedEntity(categoryRequest);
        var categoryResponse = categoryService.save(categoryRequest);

        assertNotNull(categoryResponse.getId());
        assertEquals(categoryRequest.getCode(), categoryResponse.getCode());
        assertEquals(categoryRequest.getDescription(), categoryResponse.getDescription());
    }

    @Test
    @DisplayName("Should update category when all values are valid")
    void shouldUpdateCategory_whenAllValuesAreValid(){
        var categoryRequest = getCategoryRequest();
        mockCategorySavedEntity(categoryRequest);
        var categoryResponse = categoryService.save(categoryRequest);

        assertNotNull(categoryResponse.getId());
        assertEquals(categoryRequest.getCode(), categoryResponse.getCode());
        assertEquals(categoryRequest.getDescription(), categoryResponse.getDescription());

        var categoryRequestUpdated = getCategoryRequestUpdate(categoryResponse);
        mocksCategoryUpdated(categoryRequestUpdated, categoryResponse);
        var categoryResponseUpdated = categoryService.update(categoryRequestUpdated, categoryResponse.getId());

        verify(categoryRepository, times(1)).findById(categoryResponse.getId());
        assertNotNull(categoryResponseUpdated.getId());
        assertEquals(categoryRequestUpdated.getCode(), categoryResponseUpdated.getCode());
        assertEquals(categoryRequestUpdated.getDescription(), categoryResponseUpdated.getDescription());
        assertEquals(categoryResponse.getId(), categoryResponseUpdated.getId());
        assertNotEquals(categoryResponse.getCode(), categoryResponseUpdated.getCode());
        assertNotEquals(categoryResponse.getDescription(), categoryResponseUpdated.getDescription());
    }

    @Test
    @DisplayName("Should not update category when ID is not provided")
    void shouldNotUpdateCategory_whenIdIsNotProvided(){
        var categoryRequest = getCategoryRequest();
        mockCategorySavedEntity(categoryRequest);
        var categoryResponse = categoryService.save(categoryRequest);

        assertNotNull(categoryResponse.getId());
        assertEquals(categoryRequest.getCode(), categoryResponse.getCode());
        assertEquals(categoryRequest.getDescription(), categoryResponse.getDescription());

        var categoryRequestUpdated = getCategoryRequestUpdate(categoryResponse);
        var exception = assertThrows(ValidationException.class, () -> categoryService.update(categoryRequestUpdated, null));
        assertEquals(CATEGORY_ID_MUST_BE_INFORMED, exception.getMessage());
        verify(categoryRepository, never()).findById(any());
        verify(categoryRepository, atMostOnce()).save(any());
    }

    @Test
    @DisplayName("Should not update category when category is not found")
    void shouldNotUpdateCategory_whenCategoryIsNotFound(){
        var categoryRequest = getCategoryRequest();
        mockCategorySavedEntity(categoryRequest);
        var categoryResponse = categoryService.save(categoryRequest);

        assertNotNull(categoryResponse.getId());
        assertEquals(categoryRequest.getCode(), categoryResponse.getCode());
        assertEquals(categoryRequest.getDescription(), categoryResponse.getDescription());

        var categoryRequestUpdated = getCategoryRequestUpdate(categoryResponse);
        var exception = assertThrows(ValidationException.class, () -> categoryService.update(categoryRequestUpdated, categoryResponse.getId()));
        assertEquals(CATEGORY_ID_NOT_FOUND, exception.getMessage());
        verify(categoryRepository, times(1)).findById(any());
        verify(categoryRepository, atMostOnce()).save(any());
    }

    @Test
    @DisplayName("Should find category by ID when ID exists")
    void shouldFindCategoryById_whenIdExists(){
        var categoryEntity = getEntitySaved(new CategoryEntity());
        mockCategoryFindById(categoryEntity);

        assertDoesNotThrow(() -> categoryService.findById(categoryEntity.getId()));
        verify(categoryRepository, times(2)).findById(categoryEntity.getId());
    }

    @Test
    @DisplayName("Should throw exception when ID is not provided")
    void shouldThrowException_whenIdIsNotProvided(){
        var exception = assertThrows(ValidationException.class, () -> categoryService.findById(null));
        assertEquals(CATEGORY_ID_MUST_BE_INFORMED, exception.getMessage());
        verify(categoryRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should throw exception when category is not found by ID")
    void shouldThrowException_whenCategoryIsNotFoundById(){
        var exception = assertThrows(ValidationException.class, () -> categoryService.findById(UUID.fromString("ca84af71-83e0-43b6-87f9-4aee1287cef5")));
        assertEquals(CATEGORY_ID_NOT_FOUND, exception.getMessage());
        verify(categoryRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Should throw exception when code is not provided")
    void shouldThrowException_whenCodeIsNotProvided(){
        var exception = assertThrows(ValidationException.class, () -> categoryService.findByCode(null));
        assertEquals(CATEGORY_CODE_MUST_BE_INFORMED, exception.getMessage());
        verify(categoryRepository, never()).findByCode(any());
    }

    @Test
    @DisplayName("Should throw exception when category is not found by code")
    void shouldThrowException_whenCategoryIsNotFoundByCode(){
        String codeCategory = "01";
        var exception = assertThrows(ValidationException.class, () -> categoryService.findByCode(codeCategory));
        assertEquals(CATEGORY_CODE_NOT_FOUND, exception.getMessage());
        verify(categoryRepository, times(1)).findByCode(codeCategory);
    }

    @Test
    @DisplayName("Should find category by code when code exists")
    void shouldFindCategoryByCode_whenCodeExists(){
        var categoryRequest = getCategoryRequest();
        var categoryEntity = convertCategoryFromRequestToEntity(categoryRequest);
        mockCategoryFindByCode(categoryEntity);

        assertDoesNotThrow(() -> categoryService.findByCode(categoryEntity.getCode()));
        verify(categoryRepository, times(1)).findByCode(categoryEntity.getCode());
    }

    @Test
    @DisplayName("Should throw exception when description is not provided")
    void shouldThrowException_whenDescriptionIsNotProvided(){
        var exception = assertThrows(ValidationException.class, () -> categoryService.findByDescription(null));
        assertEquals(CATEGORY_DESCRIPTION_MUST_BE_INFORMED, exception.getMessage());
        verify(categoryRepository, never()).findByDescriptionIgnoreCaseContaining(any());
    }

    @Test
    @DisplayName("Should find category by description when description exists")
    void shouldFindCategoryByDescription_whenDescriptionExists(){
        var categoryRequest = getCategoryRequest();
        mockCategoryFindByDescription(categoryRequest);
        assertDoesNotThrow(() -> categoryService.findByDescription(categoryRequest.getDescription()));
        verify(categoryRepository, times(1)).findByDescriptionIgnoreCaseContaining(any());
    }

    @Test
    @DisplayName("Should return all categories when findAll is called")
    void shouldReturnAllCategories_whenFindAllIsCalled(){
        var pageable = PageRequest.of(0, 2);
        var listCategoryEntity = getCategoryEntities();
        var categoryPage = new PageImpl<>(listCategoryEntity, pageable, 2);
        mockCategoryFindAll(pageable, categoryPage);

        var categoryPageResponse = categoryService.findAll(pageable);
        verify(categoryRepository, times(1)).findAll(pageable);
        assertEquals(1, categoryPageResponse.getTotalPages());
        assertEquals(2, categoryPageResponse.getTotalElements());
        assertEquals(2, categoryPageResponse.getContent().size());
    }

    @Test
    @DisplayName("Should throw exception when category ID is not found")
    void shouldThrowException_whenCategoryIdIsNotFound(){
        var exception = assertThrows(ValidationException.class, () -> categoryService.delete(UUID.fromString("e2cf135d-ae73-4911-b5ce-1f4402974c4c")));
        assertEquals(CATEGORY_ID_NOT_FOUND, exception.getMessage());
        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should delete category when ID exists")
    void shouldDeleteCategory_whenIdExists(){
        var categoryEntity = getCategoryEntity1();
        mockCategoryFindById(categoryEntity);
        assertDoesNotThrow(() -> categoryService.delete(categoryEntity.getId()));
        verify(categoryRepository, times(1)).deleteById(categoryEntity.getId());
    }

    private CategoryEntity getCategoryEntity1() {
        var categoryEntity = new CategoryEntity();
        categoryEntity.setId(UUID.fromString("ed9fb647-a085-48ea-9329-67d4e0da58f6"));
        categoryEntity.setCode("CODE 1");
        categoryEntity.setDescription("DESCRIPTION 1");
        return categoryEntity;
    }

    private CategoryEntity getCategoryEntity2() {
        var categoryEntity = new CategoryEntity();
        categoryEntity.setId(UUID.fromString("98fd0201-2a8c-4a9d-8a01-9bade1defaf1"));
        categoryEntity.setCode("CODE 2");
        categoryEntity.setDescription("DESCRIPTION 2");
        return categoryEntity;
    }

    private List<CategoryEntity> getCategoryEntities() {
        var categoryEntity1 = getCategoryEntity1();
        var categoryEntity2 = getCategoryEntity2();
        return List.of(categoryEntity1, categoryEntity2);
    }

    private CategoryEntity getEntityUpdated(CategoryRequest categoryRequestUpdate, CategoryResponse categoryResponse) {
        return new CategoryEntity(categoryResponse.getId(), categoryRequestUpdate.getCode(), categoryRequestUpdate.getDescription());
    }

    private CategoryEntity getEntitySaved(CategoryEntity entity) {
        var entitySaved = new CategoryEntity();
        entitySaved.setId(UUID.fromString("a4eb9c82-967d-4e55-a2f3-7fde804fcf7a"));
        entitySaved.setCode(entity.getCode());
        entitySaved.setDescription(entity.getDescription());
        return entitySaved;
    }

    private CategoryRequest getCategoryRequest() {
        CategoryRequest request = new CategoryRequest();
        request.setCode("C1");
        request.setDescription("Category T1");
        return request;
    }

    private CategoryRequest getCategoryRequestUpdate(CategoryResponse categoryResponse) {
        String newCode = "C1 Updated";
        String newDescription = "Category T1 Updated";

        CategoryRequest categoryRequestUpdate = new CategoryRequest();
        categoryRequestUpdate.setCode(newCode);
        categoryRequestUpdate.setDescription(newDescription);
        return categoryRequestUpdate;
    }

    private void mockCategoryFindByDescription(CategoryRequest categoryRequest) {
        var categoryEntity = new CategoryEntity();
        categoryEntity.setId(UUID.fromString("326171a4-2942-4106-b374-13e90d92a37e"));
        categoryEntity.setCode(categoryRequest.getCode());
        categoryEntity.setDescription(categoryRequest.getDescription());
        when(categoryRepository.findByDescriptionIgnoreCaseContaining(eq(categoryEntity.getDescription()))).thenReturn(List.of(categoryEntity));
    }

    private void mockCategoryFindById(CategoryEntity categoryEntity) {
        when(categoryRepository.findById(categoryEntity.getId())).thenReturn(Optional.of(categoryEntity));
    }

    private void mockCategoryFindByCode(CategoryEntity categoryEntity) {
        when(categoryRepository.findByCode(categoryEntity.getCode())).thenReturn(Optional.of(categoryEntity));
    }

    private void mocksCategoryUpdated(CategoryRequest categoryRequestUpdate, CategoryResponse categoryResponse) {
        var categoryEntityUpdated = getEntityUpdated(categoryRequestUpdate, categoryResponse);

        when(categoryRepository.save(argThat(categoryEntity ->
                !isNull(categoryEntity.getId()) &&
                categoryEntity.getId().equals(categoryResponse.getId())
        ))).thenReturn(categoryEntityUpdated);

        when(categoryRepository.findById(categoryResponse.getId())).thenReturn(Optional.of(categoryEntityUpdated));
    }

    private void mockCategorySavedEntity(CategoryRequest request) {
        CategoryEntity entity = new CategoryEntity();
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(getEntitySaved(entity));
    }

    private void mockCategoryFindAll(PageRequest pageable, PageImpl<CategoryEntity> categoryPage) {
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
    }

    private CategoryEntity convertCategoryFromRequestToEntity(CategoryRequest categoryRequest) {
        CategoryEntity categoryEntity = new CategoryEntity();
        BeanUtils.copyProperties(categoryRequest, categoryEntity);
        categoryEntity.setId(UUID.fromString("0451236e-9e28-4fd6-8ba9-83a79b6be099"));
        return categoryEntity;
    }
}
