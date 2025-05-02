package br.com.potential.inventory.service;

import br.com.potential.inventory.dto.request.ProductRequest;
import br.com.potential.inventory.dto.response.ProductResponse;
import br.com.potential.inventory.entity.ProductEntity;
import br.com.potential.inventory.exception.ValidationException;
import br.com.potential.inventory.interfaces.CategoryService;
import br.com.potential.inventory.interfaces.ProductService;
import br.com.potential.inventory.interfaces.SupplierService;
import br.com.potential.inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.com.potential.inventory.exception.ExceptionMessages.QUANTITY_LESS_OR_EQUAL_ZERO;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Double ZERO = 0.0;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public ProductResponse save(ProductRequest productRequest){
        validateQuantityAvailable(productRequest.getQuantityAvailable());
        var categoryEntity = categoryService.findByIdEntity(productRequest.getCategoryId());
        var supplierEntity = supplierService.findByIdEntity(productRequest.getSupplierId());
        var productEntity = productRepository.save(ProductEntity.of(productRequest, supplierEntity, categoryEntity));
        return ProductResponse.of(productEntity);
    }

    private void validateQuantityAvailable(Double quantityAvailable) {
        if(quantityAvailable <= ZERO){
            throw new ValidationException(QUANTITY_LESS_OR_EQUAL_ZERO);
        }
    }
}