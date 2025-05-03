package br.com.potential.supermarket.service;

import br.com.potential.supermarket.dto.request.SupplierRequest;
import br.com.potential.supermarket.dto.response.SupplierResponse;
import br.com.potential.supermarket.entity.SupplierEntity;
import br.com.potential.supermarket.exception.ValidationException;
import br.com.potential.supermarket.interfaces.SupplierService;
import br.com.potential.supermarket.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static br.com.potential.supermarket.exception.ExceptionMessages.SUPPLIER_ID_NOT_FOUND;

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