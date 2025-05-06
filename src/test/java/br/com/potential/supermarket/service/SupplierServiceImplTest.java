package br.com.potential.supermarket.service;

import br.com.potential.supermarket.dto.request.SupplierRequest;
import br.com.potential.supermarket.entity.SupplierEntity;
import br.com.potential.supermarket.exception.ValidationException;
import br.com.potential.supermarket.repository.SupplierRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static br.com.potential.supermarket.exception.ExceptionMessages.SUPPLIER_ID_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;
    @InjectMocks
    private SupplierServiceImpl supplierService;

    @Test
    @DisplayName("Should save supplier when all fields are filled")
    void shouldSaveSupplier_whenAllFieldsAreFilled() {
        var supplierRequest = getProductRequest();
        var supplierEntity = getProductEntity(supplierRequest);
        when(supplierRepository.save(any(SupplierEntity.class))).thenReturn(supplierEntity);
        var supplierResponse = supplierService.save(supplierRequest);

        assertNotNull(supplierResponse.getId());
        assertEquals(supplierRequest.getName(), supplierResponse.getName());
        verify(supplierRepository, times(1)).save(any(SupplierEntity.class));
    }

    @Test
    @DisplayName("Should find supplier by ID when ID exists")
    void shouldFindSupplierById_whenIdExists(){
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

    private SupplierEntity getProductEntity(SupplierRequest supplierRequest) {
        SupplierEntity entity = new SupplierEntity();
        entity.setId(UUID.randomUUID());
        entity.setName(supplierRequest.getName());
        return entity;
    }

    private SupplierRequest getProductRequest() {
        SupplierRequest request = new SupplierRequest();
        request.setName("Supplier 1");
        return request;
    }
}