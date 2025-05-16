package br.com.potential.supermarket.service;

import br.com.potential.supermarket.dto.PageResponseDto;
import br.com.potential.supermarket.dto.request.SupplierRequest;
import br.com.potential.supermarket.dto.response.SuccessResponse;
import br.com.potential.supermarket.dto.response.SupplierResponse;
import br.com.potential.supermarket.entity.SupplierEntity;
import br.com.potential.supermarket.exception.ExceptionMessages;
import br.com.potential.supermarket.exception.ValidationException;
import br.com.potential.supermarket.interfaces.ProductService;
import br.com.potential.supermarket.interfaces.SupplierService;
import br.com.potential.supermarket.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static br.com.potential.supermarket.exception.ExceptionMessages.SUPPLIER_ID_NOT_FOUND;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierServiceImpl implements SupplierService {

    private static final String SUPPLIER_DELETED = "Supplier was deleted.";

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductService productService;

    @Override
    public SupplierResponse save(SupplierRequest supplierRequest){
        var supplierEntity = supplierRepository.save(SupplierEntity.of(supplierRequest));
        return SupplierResponse.of(supplierEntity);
    }

    @Override
    public SupplierResponse update(SupplierRequest supplierRequest, UUID supplierId){
        validateInformedId(supplierId);
        SupplierEntity supplierEntity = SupplierEntity.of(supplierRequest);
        supplierEntity.setId(supplierId);
        supplierRepository.save(supplierEntity);
        return SupplierResponse.of(supplierEntity);
    }

    @Override
    public SupplierResponse findById(UUID id){
        return SupplierResponse.of(this.findByIdEntity(id));
    }

    @Override
    public List<SupplierResponse> findByName(String name) {
        return findByNameEntity(name);
    }

    @Override
    public PageResponseDto<SupplierResponse> findAll(Pageable pagination) {
        var supplierPage = supplierRepository.findAll(pagination)
                .map(SupplierResponse::of);

        return new PageResponseDto<>(
                supplierPage.getContent(),
                supplierPage.getTotalElements(),
                supplierPage.getTotalPages());
    }

    @Override
    public SupplierEntity findByIdEntity(UUID id){
        return supplierRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException(SUPPLIER_ID_NOT_FOUND));
    }

    @Override
    public SuccessResponse delete(UUID supplierId){
        validateInformedId(supplierId);
        if(productService.existsBySupplierId(supplierId)){
            throw new ValidationException(ExceptionMessages.SUPPLIER_DEFINED_IN_PRODUCT);
        }
        supplierRepository.deleteById(supplierId);
        return SuccessResponse.create(SUPPLIER_DELETED);
    }

    private void validateInformedId(UUID supplierId) {
        if(isEmpty(supplierId)){
            throw new ValidationException(ExceptionMessages.SUPPLIER_ID_MUST_BE_INFORMED);
        }
    }

    private List<SupplierResponse> findByNameEntity(String name) {
        if(isEmpty(name)){
            throw new ValidationException(ExceptionMessages.SUPPLIER_NAME_MUST_BE_INFORMED);
        }

        return supplierRepository.findByNameIgnoreCaseContaining(name)
                .stream()
                .map(SupplierResponse::of)
                .collect(Collectors.toList());
    }
}