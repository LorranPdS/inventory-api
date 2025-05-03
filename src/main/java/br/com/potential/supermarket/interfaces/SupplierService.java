package br.com.potential.supermarket.interfaces;

import br.com.potential.supermarket.dto.request.SupplierRequest;
import br.com.potential.supermarket.dto.response.SupplierResponse;
import br.com.potential.supermarket.entity.SupplierEntity;

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
