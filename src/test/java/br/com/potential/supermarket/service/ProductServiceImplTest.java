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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.potential.supermarket.exception.ExceptionMessages.*;
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
        assertEquals(QUANTITY_SHOULD_NOT_BE_LESS_THAN_OR_EQUAL_TO_ZERO, exception.getMessage());
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

    @Test
    @DisplayName("Should throw an exception in Product when product was not found")
    void shoudThrowAnExceptionProduct_WhenProductWasNotFound(){
        var productId = UUID.randomUUID();
        var exception = assertThrows(ValidationException.class, () -> productService.findByIdEntity(productId));
        assertEquals(PRODUCT_ID_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Should return ProductEntity when id exists")
    void shoudReturnProductEntity_WhenIdExists(){
        var productId = UUID.randomUUID();
        Optional<ProductEntity> productEntity = getProductEntity(productId);
        when(productRepository.findById(productId)).thenReturn(productEntity);
        var response = productService.findByIdEntity(productId);
        assertEquals(productEntity.get().getId(), response.getId());
        assertEquals(productEntity.get().getName(), response.getName());
    }

    @Test
    @DisplayName("Should return ProductResponse when id exists")
    void shoudReturnProductResponse_WhenIdExists(){
        var productId = UUID.randomUUID();
        Optional<ProductEntity> productEntityOpt = getProductEntity(productId);
        when(productRepository.findById(productId)).thenReturn(productEntityOpt);
        var response = productService.findById(productId);
        assertEquals(productEntityOpt.get().getId(), response.getId());
        assertEquals(productEntityOpt.get().getName(), response.getName());
    }

    @Test
    @DisplayName("Should throw an exception in Product when name informed by parameter is empty")
    void shoudThrowAnExceptionProduct_WhenNameInformedByParameterIsEmpty(){
        var name = "";
        var exception = assertThrows(ValidationException.class, () -> productService.findByName(name));
        verify(productRepository, never()).findByNameIgnoreCaseContaining(any());
        assertEquals(PRODUCT_NAME_MUST_BE_INFORMED, exception.getMessage());
    }

    @Test
    @DisplayName("Should return ProductResponse list when name exists")
    void shoudReturnProductResponseList_WhenNameExists(){
        var productId = UUID.randomUUID();
        Optional<ProductEntity> productEntityOpt = getProductEntity(productId);
        var productEntity = productEntityOpt.get();
        when(productRepository.findByNameIgnoreCaseContaining(productEntity.getName())).thenReturn(List.of(productEntity));
        var listProductResponse = productService.findByName(productEntity.getName());
        assertEquals(1, listProductResponse.size());
        assertEquals(productEntity.getName(), listProductResponse.get(0).getName());
    }

    @Test
    @DisplayName("Should throw Product exception when ID is not provided")
    void shouldThrowProductException_WhenIdIsNotProvided(){
        UUID productId = null;
        var exception = assertThrows(ValidationException.class, () -> productService.update(new ProductRequest(), productId));
        assertEquals(PRODUCT_ID_MUST_BE_INFORMED, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, 0.0})
    @DisplayName("Should throw Product exception when quantity available is less than or equal to zero")
    void shouldThrowProductException_WhenQuantityAvailableIsLessThanOrEqualToZero(double quantityAvailable){
        var productRequest = getProductRequest();
        productRequest.setQuantityAvailable(quantityAvailable);
        var productId = UUID.randomUUID();
        var exception = assertThrows(ValidationException.class, () -> productService.update(productRequest, productId));
        assertEquals(QUANTITY_SHOULD_NOT_BE_LESS_THAN_OR_EQUAL_TO_ZERO, exception.getMessage());
    }

    @Test
    @DisplayName("Should update Product when all values are valid")
    void shouldUpdateProduct_WhenAllValuesAreValid(){
        var productRequest = getProductRequest();
        var productEntity = getProductEntity(productRequest);
        mockProductSaved(productEntity);
        var productSaved = productService.save(productRequest);

        assertNotNull(productSaved.getId());
        assertEquals(productRequest.getName(), productSaved.getName());
        assertEquals(productRequest.getQuantityAvailable(), productSaved.getQuantityAvailable());

        var productRequestUpdate = productRequestUpdated(productRequest);
        var productUpdated = productService.update(productRequestUpdate, productEntity.getId());
        assertEquals(productSaved.getId(), productUpdated.getId());
        assertNotEquals(productSaved.getName(), productUpdated.getName());
        assertNotEquals(productSaved.getQuantityAvailable(), productUpdated.getQuantityAvailable());
    }

    private ProductRequest productRequestUpdated(ProductRequest productRequest) {
        productRequest.setName("Product Updated");
        productRequest.setQuantityAvailable(15.0);
        return productRequest;
    }

    private Optional<ProductEntity> getProductEntity(UUID productId) {
        var productEntity = new ProductEntity();
        productEntity.setId(productId);
        productEntity.setName("Product A");
        productEntity.setQuantityAvailable(1.0);
        productEntity.setSupplierEntity(getSupplierEntity(UUID.randomUUID()));
        productEntity.setCategoryEntity(getCategoryEntity(UUID.randomUUID()));
        return Optional.of(productEntity);
    }

    private ProductEntity getProductEntity(ProductRequest productRequest) {
        var entity = new ProductEntity();
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