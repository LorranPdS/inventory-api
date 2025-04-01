package br.com.potential.inventory.service;

import br.com.potential.inventory.dto.CategoryRequest;
import br.com.potential.inventory.dto.CategoryResponse;
import br.com.potential.inventory.entity.CategoryEntity;
import br.com.potential.inventory.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryServiceImpl service;

    @Test
    @DisplayName("Save if all fields are filled")
    void testShouldSaveCategory_WhenAllFieldsAreFilled() {
        var request = getCategoryRequest();
        mockCategoryEntity(request);
        CategoryResponse response = service.save(request);
        assertNotNull(response.getId());
        assertEquals(request.getCode(), response.getCode());
        assertEquals(request.getDescription(), response.getDescription());
    }

    private void mockCategoryEntity(CategoryRequest request) {
        CategoryEntity entity = new CategoryEntity();
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        when(repository.save(any(CategoryEntity.class))).thenReturn(getEntitySaved(entity));
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