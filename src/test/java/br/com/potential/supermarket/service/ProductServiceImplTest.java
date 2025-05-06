package br.com.potential.supermarket.service;

import br.com.potential.supermarket.dto.request.ProductRequest;
import br.com.potential.supermarket.entity.CategoryEntity;
import br.com.potential.supermarket.entity.ProductEntity;
import br.com.potential.supermarket.entity.SupplierEntity;
import br.com.potential.supermarket.exception.ValidationException;
import br.com.potential.supermarket.interfaces.CategoryService;
import br.com.potential.supermarket.interfaces.SupplierService;
import br.com.potential.supermarket.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static br.com.potential.supermarket.exception.ExceptionMessages.QUANTITY_LESS_OR_EQUAL_ZERO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private SupplierService supplierService;
    @Mock
    private CategoryService categoryService;
    @InjectMocks
    private ProductServiceImpl productService;

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, 0.0})
    @DisplayName("Should throw an exception when the available quantity is less than or equal to zero")
    void shouldThrowExceptionProduct_whenAvailableQuantityIsLessThanOrEqualToZero(double invalidQuantity) {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setQuantityAvailable(invalidQuantity);
        var exception = assertThrows(ValidationException.class, () -> productService.save(productRequest));
        verify(productRepository, never()).save(any());
        assertEquals(QUANTITY_LESS_OR_EQUAL_ZERO, exception.getMessage());
    }

    @Test
    @DisplayName("Should save product when all fields are filled")
    void shouldSaveProduct_whenAllFieldsAreFilled() {
        var productRequest = getProductRequest();
        var productEntity = getProductEntity(productRequest);
        mockProductSaved(productEntity);
        var productResponse = productService.save(productRequest);

        assertNotNull(productResponse.getId());
        assertEquals(productRequest.getName(), productResponse.getName());
        assertEquals(productRequest.getQuantityAvailable(), productResponse.getQuantityAvailable());
        assertEquals(productRequest.getCategoryId(), productResponse.getCategory().getId());
        assertEquals(productRequest.getSupplierId(), productResponse.getSupplier().getId());
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    private ProductEntity getProductEntity(ProductRequest productRequest) {
        ProductEntity entity = new ProductEntity();
        CategoryEntity categoryEntity = getCategoryEntity(productRequest.getCategoryId());
        SupplierEntity supplierEntity = getSupplierEntity(productRequest.getSupplierId());

        entity.setId(UUID.randomUUID());
        entity.setName(productRequest.getName());
        entity.setQuantityAvailable(productRequest.getQuantityAvailable());
        entity.setCategoryEntity(categoryEntity);
        entity.setSupplierEntity(supplierEntity);
        return entity;
    }

    private void mockProductSaved(ProductEntity productEntity) {
        when(categoryService.findByIdEntity(productEntity.getCategoryEntity().getId())).thenReturn(productEntity.getCategoryEntity());
        when(supplierService.findByIdEntity(productEntity.getSupplierEntity().getId())).thenReturn(productEntity.getSupplierEntity());
        when(productRepository.save(any())).thenReturn(productEntity);
    }

    private CategoryEntity getCategoryEntity(UUID categoryId) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryId);
        categoryEntity.setCode("OCAR");
        categoryEntity.setDescription("Oral Care");
        return categoryEntity;
    }

    private SupplierEntity getSupplierEntity(UUID supplierId) {
        SupplierEntity supplierEntity = new SupplierEntity();
        supplierEntity.setId(supplierId);
        supplierEntity.setName("Supplier 1");
        return supplierEntity;
    }

    private ProductRequest getProductRequest() {
        ProductRequest request = new ProductRequest();
        request.setName("toothpaste");
        request.setQuantityAvailable(12.0);
        request.setSupplierId(UUID.randomUUID());
        request.setCategoryId(UUID.randomUUID());
        return request;
    }
}