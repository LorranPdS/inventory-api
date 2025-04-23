package br.com.potential.inventory.service;

import br.com.potential.inventory.dto.request.SupplierRequest;
import br.com.potential.inventory.dto.response.SupplierResponse;
import br.com.potential.inventory.entity.SupplierEntity;
import br.com.potential.inventory.interfaces.SupplierService;
import br.com.potential.inventory.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public SupplierResponse save(SupplierRequest supplierRequest){
        var supplierEntity = supplierRepository.save(SupplierEntity.of(supplierRequest));
        return SupplierResponse.of(supplierEntity);
    }
}