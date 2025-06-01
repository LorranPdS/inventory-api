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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.potential.supermarket.exception.ExceptionMessages.*;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
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
        Optional<ProductEntity> productEntity = getProductEntityOpt(productId);
        when(productRepository.findById(productId)).thenReturn(productEntity);
        var response = productService.findByIdEntity(productId);
        assertEquals(productEntity.get().getId(), response.getId());
        assertEquals(productEntity.get().getName(), response.getName());
    }

    @Test
    @DisplayName("Should return ProductResponse when id exists")
    void shoudReturnProductResponse_WhenIdExists(){
        var productId = UUID.randomUUID();
        Optional<ProductEntity> productEntityOpt = getProductEntityOpt(productId);
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
        Optional<ProductEntity> productEntityOpt = getProductEntityOpt(productId);
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

    @Test
    @DisplayName("Should throw Product exception when categoryId is not provided in findByCategoryId")
    void shouldThrowProductException_WhenCategoryIdIsNotProvidedInFindByCategoryId(){
        var exception = assertThrows(ValidationException.class, () -> productService.findByCategoryId(null));
        assertEquals(CATEGORY_ID_MUST_BE_INFORMED, exception.getMessage());
    }

    @Test
    @DisplayName("Should return all ProductResponse that have the category informed")
    void shoudReturnAllProductResponse_ThatHaveCategoryInformed(){
        var productEntity = getProductEntity(getProductRequest());
        var categoryEntity = productEntity.getCategoryEntity();
        when(productRepository.findByCategoryEntityId(categoryEntity.getId())).thenReturn(List.of(productEntity));
        var response = productService.findByCategoryId(categoryEntity.getId());
        assertEquals(1, response.size());
        assertEquals(productEntity.getId(), response.get(0).getId());
        assertEquals(categoryEntity.getId(), response.get(0).getCategory().getId());
    }

    @Test
    @DisplayName("Should throw Product exception when supplierId is not provided in findBySupplierId")
    void shouldThrowProductException_WhenSupplierIdIsNotProvidedInFindBySupplierId(){
        var exception = assertThrows(ValidationException.class, () -> productService.findBySupplierId(null));
        assertEquals(SUPPLIER_ID_MUST_BE_INFORMED, exception.getMessage());
    }

    @Test
    @DisplayName("Should return all ProductResponse that have the supplier informed")
    void shouldReturnAllProductResponse_ThatHaveSupplierInformed(){
        var productEntity = getProductEntity(getProductRequest());
        var supplierEntity = productEntity.getSupplierEntity();
        when(productRepository.findBySupplierEntityId(supplierEntity.getId())).thenReturn(List.of(productEntity));
        var response = productService.findBySupplierId(supplierEntity.getId());
        assertEquals(1, response.size());
        assertEquals(productEntity.getId(), response.get(0).getId());
        assertEquals(supplierEntity.getId(), response.get(0).getSupplier().getId());
    }

    @Test
    @DisplayName("Should throw exception on exclusion in Product when ID is not provided")
    void shouldThrowExceptionExclusionProduct_WhenIdIsNotProvided(){
        var exception = assertThrows(ValidationException.class, () -> productService.delete(null));
        assertEquals(PRODUCT_ID_MUST_BE_INFORMED, exception.getMessage());
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should return SuccessResponse when product is deleted")
    void shouldReturnSuccessResponse_WhenProductIsDeleted(){
        var productId = UUID.fromString("f10adab6-6781-4499-8c0f-5d47fed8a1a7");
        var response = productService.delete(productId);
        verify(productRepository, times(1)).deleteById(productId);
        assertEquals("Product was deleted.", response.getMessage());
        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("Should return TRUE when exists product with this supplierId")
    void shouldReturnTrue_WhenExistsProductWithThisSupplierId(){
        var supplierId = UUID.fromString("6f07bab4-5879-4010-bc05-e279d9199de1");
        when(productRepository.existsBySupplierEntityId(supplierId)).thenReturn(TRUE);
        assertTrue(productService.existsBySupplierId(supplierId));
    }

    @Test
    @DisplayName("Should return TRUE when exists product with this categoryId")
    void shouldReturnTrue_WhenExistsProductWithThisCategoryId(){
        var categoryId = UUID.fromString("2bde780f-b189-4570-b4f7-b45e451ef8df");
        when(productRepository.existsByCategoryEntityId(categoryId)).thenReturn(TRUE);
        assertTrue(productService.existsByCategoryId(categoryId));
    }

    @Test
    @DisplayName("Should return FALSE when not exists product with this supplierId")
    void shouldReturnFalse_WhenNotExistsProductWithThisSupplierId(){
        var supplierId = UUID.fromString("ebaaf286-1e4d-43cb-ac7b-2e4dc30be1cc");
        when(productRepository.existsBySupplierEntityId(supplierId)).thenReturn(FALSE);
        assertFalse(productService.existsBySupplierId(supplierId));
    }

    @Test
    @DisplayName("Should return FALSE when not exists product with this categoryId")
    void shouldReturnFalse_WhenNotExistsProductWithThisCategoryId(){
        var categoryId = UUID.fromString("5dad0bb5-a9ad-4c2d-a082-9164ba9b185b");
        when(productRepository.existsByCategoryEntityId(categoryId)).thenReturn(FALSE);
        assertFalse(productService.existsByCategoryId(categoryId));
    }

    @Test
    @DisplayName("Should return all products when findAll is called")
    void shouldReturnAllProducts_whenFindAllIsCalled(){
        var pageable = PageRequest.of(0, 2);
        var listProductEntity = getProductEntities();
        var productPage = new PageImpl<>(listProductEntity, pageable, 2);
        mockProductFindAll(pageable, productPage);

        var categoryPageResponse = productService.findAll(pageable);
        verify(productRepository, times(1)).findAll(pageable);
        assertEquals(1, categoryPageResponse.getTotalPages());
        assertEquals(2, categoryPageResponse.getTotalElements());
        assertEquals(2, categoryPageResponse.getContent().size());
    }

    private void mockProductFindAll(PageRequest pageable, PageImpl<ProductEntity> productPage) {
        when(productRepository.findAll(pageable)).thenReturn(productPage);
    }

    private List<ProductEntity> getProductEntities() {
        var productRequest1 = new ProductRequest();
        productRequest1.setName("toothbrush");
        productRequest1.setQuantityAvailable(50.0);
        productRequest1.setCategoryId(UUID.fromString("28b823cb-3cb1-4373-a001-a7aeae56fbce"));
        productRequest1.setSupplierId(UUID.fromString("0cd6165d-28a1-47b5-905d-56fe1d3007b4"));

        var productEntity1 = getProductEntity(productRequest1);

        var productRequest2 = new ProductRequest();
        productRequest2.setName("dental floss");
        productRequest2.setQuantityAvailable(60.0);
        productRequest2.setCategoryId(productRequest1.getCategoryId());
        productRequest2.setSupplierId(productRequest1.getSupplierId());
        var productEntity2 = getProductEntity(productRequest2);

        return List.of(productEntity1, productEntity2);
    }

    private ProductRequest productRequestUpdated(ProductRequest productRequest) {
        productRequest.setName("Product Updated");
        productRequest.setQuantityAvailable(15.0);
        return productRequest;
    }

    private Optional<ProductEntity> getProductEntityOpt(UUID productId) {
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