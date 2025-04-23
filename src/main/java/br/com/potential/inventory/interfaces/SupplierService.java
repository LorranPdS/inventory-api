package br.com.potential.inventory.interfaces;

import br.com.potential.inventory.dto.request.SupplierRequest;
import br.com.potential.inventory.dto.response.SupplierResponse;

public interface SupplierService {

    SupplierResponse save(SupplierRequest supplierRequest);

//    SupplierResponse update(SupplierRequest supplierRequest, UUID id);
//
//    SupplierResponse findById(UUID id);
//
//    List<SupplierResponse> findByName(String name);
//
//    PageResponseDto<SupplierResponse> findAll(Pageable pagination);
//
//    void delete(UUID id);
}
