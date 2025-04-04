package br.com.potential.inventory.service;

import br.com.potential.inventory.dto.CategoryRequest;
import br.com.potential.inventory.dto.CategoryResponse;
import br.com.potential.inventory.entity.CategoryEntity;
import br.com.potential.inventory.exception.ValidationException;
import br.com.potential.inventory.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static br.com.potential.inventory.exception.ExceptionMessages.CATEGORY_ID_MUST_BE_INFORMED;
import static br.com.potential.inventory.exception.ExceptionMessages.CATEGORY_ID_NOT_FOUND;
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
    @DisplayName("Save if all fields are filled")
    void testShouldSaveCategory_WhenAllFieldsAreFilled() {
        var categoryRequest = getCategoryRequest();
        mockCategorySavedEntity(categoryRequest);
        var categoryResponse = categoryService.save(categoryRequest);

        assertNotNull(categoryResponse.getId());
        assertEquals(categoryRequest.getCode(), categoryResponse.getCode());
        assertEquals(categoryRequest.getDescription(), categoryResponse.getDescription());
    }

    @Test
    @DisplayName("Update category with all values are right")
    public void testShouldUpdateCategory_WhenAllValuesAreRight(){
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
    @DisplayName("It should throw an exception when the ID Category is not provided")
    public void testShouldThrowExceptionCategory_WhenIdIsNotProvided(){
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
    }

    @Test
    @DisplayName("It should throw an exception when the category is not found")
    public void testShouldThrowExceptionCategory_WhenCategoryIsNotFound(){
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

    private CategoryRequest getCategoryRequestUpdate(CategoryResponse categoryResponse) {
        String newCode = "C1 Updated";
        String newDescription = "Category T1 Updated";

        CategoryRequest categoryRequestUpdate = new CategoryRequest();
        categoryRequestUpdate.setCode(newCode);
        categoryRequestUpdate.setDescription(newDescription);
        return categoryRequestUpdate;
    }

    private void mocksCategoryUpdated(CategoryRequest categoryRequestUpdate, CategoryResponse categoryResponse) {
        var categoryEntityUpdated = getEntityUpdated(categoryRequestUpdate, categoryResponse);

        when(categoryRepository.save(argThat(categoryEntity ->
                !isNull(categoryEntity.getId()) &&
                categoryEntity.getId().equals(categoryResponse.getId())
        ))).thenReturn(categoryEntityUpdated);

        when(categoryRepository.findById(categoryResponse.getId())).thenReturn(Optional.of(categoryEntityUpdated));
    }

    private CategoryEntity getEntityUpdated(CategoryRequest categoryRequestUpdate, CategoryResponse categoryResponse) {
        return new CategoryEntity(categoryResponse.getId(), categoryRequestUpdate.getCode(), categoryRequestUpdate.getDescription());
    }

    private void mockCategorySavedEntity(CategoryRequest request) {
        CategoryEntity entity = new CategoryEntity();
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(getEntitySaved(entity));
    }

    private CategoryEntity getEntitySaved(CategoryEntity entity) {
        var entitySaved = new CategoryEntity();
        entitySaved.setId(UUID.randomUUID());
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
}