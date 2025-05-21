package br.com.potential.supermarket.service;

import br.com.potential.supermarket.dto.request.SupplierRequest;
import br.com.potential.supermarket.dto.response.SupplierResponse;
import br.com.potential.supermarket.entity.SupplierEntity;
import br.com.potential.supermarket.exception.ExceptionMessages;
import br.com.potential.supermarket.exception.ValidationException;
import br.com.potential.supermarket.interfaces.ProductService;
import br.com.potential.supermarket.repository.SupplierRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.potential.supermarket.exception.ExceptionMessages.*;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.isNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceImplTest {

    @Mock
    private ProductService productService;
    @Mock
    private SupplierRepository supplierRepository;
    @InjectMocks
    private SupplierServiceImpl supplierService;

    @Test
    @DisplayName("Should save supplier when all fields are filled")
    void shouldSaveSupplier_whenAllFieldsAreFilled() {
        var supplierRequest = getSupplierRequest();
        var supplierEntity = getSupplierEntitySaved(supplierRequest);
        when(supplierRepository.save(any(SupplierEntity.class))).thenReturn(supplierEntity);
        var supplierResponse = supplierService.save(supplierRequest);

        assertNotNull(supplierResponse.getId());
        assertEquals(supplierRequest.getName(), supplierResponse.getName());
        verify(supplierRepository, times(1)).save(any(SupplierEntity.class));
    }

    @Test
    @DisplayName("Should update supplier when all values are valid")
    void shouldUpdateSupplier_whenAllValuesAreValid(){
        var supplierRequest = getSupplierRequest();
        mockSupplierSavedEntity(supplierRequest);
        var supplierResponse = supplierService.save(supplierRequest);

        assertNotNull(supplierResponse.getId());
        assertEquals(supplierRequest.getName(), supplierResponse.getName());

        var supplierRequestUpdated = getSupplierRequestUpdate(supplierResponse);
        mocksSupplierUpdated(supplierRequestUpdated, supplierResponse);
        var supplierResponseUpdated = supplierService.update(supplierRequestUpdated, supplierResponse.getId());

        assertNotNull(supplierResponseUpdated.getId());
        assertEquals(supplierRequestUpdated.getName(), supplierResponseUpdated.getName());
        assertEquals(supplierResponse.getId(), supplierResponseUpdated.getId());
        assertNotEquals(supplierResponse.getName(), supplierResponseUpdated.getName());
    }

    @Test
    @DisplayName("Should find supplier by method 'findByIdEntity' when ID exists")
    void shouldFindSupplierByMethodFindByIdEntity_whenIdExists(){
        var supplierEntity = new SupplierEntity(UUID.randomUUID(), "Supplier 1");
        when(supplierRepository.findById(supplierEntity.getId())).thenReturn(Optional.of(supplierEntity));
        assertDoesNotThrow(() -> supplierService.findByIdEntity(supplierEntity.getId()));

        verify(supplierRepository, times(1)).findById(supplierEntity.getId());
    }

    @Test
    @DisplayName("Should throw an exception when supplier was not found")
    void shouldThrowAnException_whenSupplierWasNotFound(){
        var supplierEntity = new SupplierEntity(UUID.randomUUID(), "Supplier 1");
        var exception = assertThrows(ValidationException.class, () -> supplierService.findByIdEntity(supplierEntity.getId()));
        assertEquals(SUPPLIER_ID_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Should find supplier by method 'findById' when ID exists")
    void shouldFindSupplierByMethodFindById_WhenIdExists(){
        var supplierEntity = getSupplierEntity1();
        when(supplierRepository.findById(supplierEntity.getId())).thenReturn(Optional.of(supplierEntity));
        var supplierResponse = supplierService.findById(supplierEntity.getId());
        verify(supplierRepository, times(1)).findById(supplierEntity.getId());
        assertEquals(supplierEntity.getId(), supplierResponse.getId());
        assertEquals(supplierEntity.getName(), supplierResponse.getName());
    }

    @Test
    @DisplayName("Should find supplier by name when name exists")
    void shouldFindSupplierByName_WhenNameExists(){
        var supplierEntity = getSupplierEntity1();
        when(supplierRepository.findByNameIgnoreCaseContaining(supplierEntity.getName())).thenReturn(List.of(supplierEntity));
        var supplierResponse = supplierService.findByName(supplierEntity.getName());
        verify(supplierRepository, times(1)).findByNameIgnoreCaseContaining(supplierEntity.getName());
        assertEquals(supplierEntity.getId(), supplierResponse.get(0).getId());
        assertEquals(supplierEntity.getName(), supplierResponse.get(0).getName());
    }

    @Test
    @DisplayName("Should throw an exception when 'name' is not provided")
    void shouldThrowException_WhenNameIsNotProvided(){
        var nameNotInformed = "";
        var exception = assertThrows(ValidationException.class, () -> supplierService.findByName(nameNotInformed));
        assertEquals(SUPPLIER_NAME_MUST_BE_INFORMED, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw an exception when ID is not provided")
    void shouldThrowException_WhenIdIsNotProvided(){
        var supplierRequest = getSupplierRequest();
        UUID supplierId = null;
        var exception = assertThrows(ValidationException.class, () -> supplierService.update(supplierRequest, supplierId));
        assertEquals(SUPPLIER_ID_MUST_BE_INFORMED, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw an exception when supplier is defined in a product")
    void shouldThrowException_WhenSupplierIsDefinedInProduct(){
        var supplierId = UUID.randomUUID();
        when(productService.existsBySupplierId(supplierId)).thenReturn(TRUE);
        var exception = assertThrows(ValidationException.class, () -> supplierService.delete(supplierId));
        assertEquals(ExceptionMessages.SUPPLIER_DEFINED_IN_PRODUCT, exception.getMessage());
    }

    @Test
    @DisplayName("Should delete supplier when all values are valid")
    void shouldDeleteSupplier_WhenAllValuesAreValid(){
        var supplierId = UUID.randomUUID();
        when(productService.existsBySupplierId(supplierId)).thenReturn(Boolean.FALSE);
        var response = supplierService.delete(supplierId);
        assertEquals("Supplier was deleted.", response.getMessage());
    }

    @Test
    @DisplayName("Should find all suppliers when suppliers are saved")
    void shouldFindAllSuppliers_WhenExistsSuppliersSaved() {
        Pageable pageable = PageRequest.of(0, 10);
        var supplier1 = getSupplierEntity1();
        var supplier2 = getSupplierEntity2();
        var supplierPage = new PageImpl<>(List.of(supplier1, supplier2), pageable, 2);
        when(supplierRepository.findAll(pageable)).thenReturn(supplierPage);

        var response = supplierService.findAll(pageable);

        assertNotNull(response);
        assertEquals(2, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(2, response.getContent().size());
        assertEquals(supplier1.getName(), response.getContent().get(0).getName());
        assertEquals(supplier2.getName(), response.getContent().get(1).getName());
    }

    private SupplierEntity getSupplierEntity1() {
        var supplierEntity = new SupplierEntity();
        supplierEntity.setId(UUID.randomUUID());
        supplierEntity.setName("Supplier 1");
        return supplierEntity;
    }

    private SupplierEntity getSupplierEntity2() {
        var supplierEntity = new SupplierEntity();
        supplierEntity.setId(UUID.randomUUID());
        supplierEntity.setName("Supplier 2");
        return supplierEntity;
    }

    private SupplierEntity getEntityUpdated(SupplierRequest supplierRequestUpdate, SupplierResponse supplierResponse) {
        return new SupplierEntity(supplierResponse.getId(), supplierRequestUpdate.getName());
    }

    private SupplierRequest getSupplierRequestUpdate(SupplierResponse supplierResponse) {
        String newName = "Supplier Updated";

        SupplierRequest supplierRequestUpdate = new SupplierRequest();
        supplierRequestUpdate.setName(newName);
        return supplierRequestUpdate;
    }

    private SupplierEntity getEntitySaved(SupplierEntity entity) {
        var entitySaved = new SupplierEntity();
        entitySaved.setId(UUID.randomUUID());
        entitySaved.setName(entity.getName());
        return entitySaved;
    }

    private SupplierEntity getSupplierEntitySaved(SupplierRequest supplierRequest) {
        SupplierEntity entity = new SupplierEntity();
        entity.setId(UUID.randomUUID());
        entity.setName(supplierRequest.getName());
        return entity;
    }

    private SupplierRequest getSupplierRequest() {
        SupplierRequest request = new SupplierRequest();
        request.setName("Supplier 1");
        return request;
    }

    private void mockSupplierSavedEntity(SupplierRequest request) {
        SupplierEntity entity = new SupplierEntity();
        entity.setName(request.getName());
        when(supplierRepository.save(any(SupplierEntity.class))).thenReturn(getEntitySaved(entity));
    }

    private void mocksSupplierUpdated(SupplierRequest supplierRequestUpdate, SupplierResponse supplierResponse) {
        var supplierEntityUpdated = getEntityUpdated(supplierRequestUpdate, supplierResponse);

        when(supplierRepository.save(argThat(supplierEntity ->
                !isNull(supplierEntity.getId()) &&
                        supplierEntity.getId().equals(supplierResponse.getId())
        ))).thenReturn(supplierEntityUpdated);
    }
}