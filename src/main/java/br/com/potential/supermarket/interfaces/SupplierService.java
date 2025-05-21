package br.com.potential.supermarket.interfaces;

import br.com.potential.supermarket.dto.PageResponseDto;
import br.com.potential.supermarket.dto.request.SupplierRequest;
import br.com.potential.supermarket.dto.response.SuccessResponse;
import br.com.potential.supermarket.dto.response.SupplierResponse;
import br.com.potential.supermarket.entity.SupplierEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface SupplierService {

    SupplierResponse save(SupplierRequest supplierRequest);

    SupplierResponse update(SupplierRequest supplierRequest, UUID id);

    SupplierResponse findById(UUID id);

    SupplierEntity findByIdEntity(UUID id);

    List<SupplierResponse> findByName(String name);

    PageResponseDto<SupplierResponse> findAll(Pageable pagination);

    SuccessResponse delete(UUID id);
}
