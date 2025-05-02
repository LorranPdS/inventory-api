package br.com.potential.inventory.interfaces;

import br.com.potential.inventory.dto.request.SupplierRequest;
import br.com.potential.inventory.dto.response.SupplierResponse;
import br.com.potential.inventory.entity.SupplierEntity;

import java.util.UUID;

public interface SupplierService {

    SupplierResponse save(SupplierRequest supplierRequest);

    SupplierEntity findByIdEntity(UUID id);

//    SupplierResponse update(SupplierRequest supplierRequest, UUID id);
//
//
//    List<SupplierResponse> findByName(String name);
//
//    PageResponseDto<SupplierResponse> findAll(Pageable pagination);
//
//    void delete(UUID id);
}
