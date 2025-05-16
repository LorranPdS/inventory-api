package br.com.potential.supermarket.controller;

import br.com.potential.supermarket.dto.PageResponseDto;
import br.com.potential.supermarket.dto.request.SupplierRequest;
import br.com.potential.supermarket.dto.response.SuccessResponse;
import br.com.potential.supermarket.dto.response.SupplierResponse;
import br.com.potential.supermarket.interfaces.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Here are my endpoints related to Supplier.
 */
@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping
    public SupplierResponse save(@RequestBody @Valid SupplierRequest supplierRequest){
        return supplierService.save(supplierRequest);
    }

    @GetMapping("{id}")
    public SupplierResponse findById(@PathVariable(name = "id") UUID supplierId) {
        return supplierService.findById(supplierId);
    }

    @GetMapping("name/{name}")
    public List<SupplierResponse> findByName(@PathVariable String name){
        return supplierService.findByName(name);
    }

    @GetMapping
    public PageResponseDto<SupplierResponse> findAll(@PageableDefault(size = 10) Pageable pagination) {
        return supplierService.findAll(pagination);
    }

    @PutMapping("{supplierId}")
    public SupplierResponse update(@RequestBody @Valid SupplierRequest supplierRequest, @PathVariable UUID supplierId){
        return supplierService.update(supplierRequest, supplierId);
    }

    @DeleteMapping("{id}")
    public SuccessResponse delete(@PathVariable(name = "id") UUID supplierId){
        return supplierService.delete(supplierId);
    }
}
