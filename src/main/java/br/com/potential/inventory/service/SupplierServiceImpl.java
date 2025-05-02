package br.com.potential.inventory.service;

import br.com.potential.inventory.dto.request.SupplierRequest;
import br.com.potential.inventory.dto.response.SupplierResponse;
import br.com.potential.inventory.entity.SupplierEntity;
import br.com.potential.inventory.exception.ValidationException;
import br.com.potential.inventory.interfaces.SupplierService;
import br.com.potential.inventory.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static br.com.potential.inventory.exception.ExceptionMessages.SUPPLIER_ID_NOT_FOUND;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public SupplierResponse save(SupplierRequest supplierRequest){
        var supplierEntity = supplierRepository.save(SupplierEntity.of(supplierRequest));
        return SupplierResponse.of(supplierEntity);
    }

    @Override
    public SupplierEntity findByIdEntity(UUID id){
        return supplierRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException(SUPPLIER_ID_NOT_FOUND));
    }
}