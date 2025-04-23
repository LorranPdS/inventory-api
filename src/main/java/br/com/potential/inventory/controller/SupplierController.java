package br.com.potential.inventory.controller;

import br.com.potential.inventory.dto.request.SupplierRequest;
import br.com.potential.inventory.dto.response.SupplierResponse;
import br.com.potential.inventory.interfaces.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
