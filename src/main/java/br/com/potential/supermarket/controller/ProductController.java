package br.com.potential.supermarket.controller;

import br.com.potential.supermarket.dto.PageResponseDto;
import br.com.potential.supermarket.dto.request.ProductRequest;
import br.com.potential.supermarket.dto.response.ProductResponse;
import br.com.potential.supermarket.dto.response.SuccessResponse;
import br.com.potential.supermarket.interfaces.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Here are my endpoints related to Product.
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ProductResponse save(@RequestBody @Valid ProductRequest productRequest){
        return productService.save(productRequest);
    }

    @GetMapping("{id}")
    public ProductResponse findById(@PathVariable(name = "id") UUID supplierId) {
        return productService.findById(supplierId);
    }

    @GetMapping("name/{name}")
    public List<ProductResponse> findByName(@PathVariable String name){
        return productService.findByName(name);
    }

    @GetMapping("category/{categoryId}")
    public List<ProductResponse> findByCategoryId(@PathVariable UUID categoryId){
        return productService.findByCategoryId(categoryId);
    }

    @GetMapping("supplier/{supplierId}")
    public List<ProductResponse> findBySupplierId(@PathVariable UUID supplierId){
        return productService.findBySupplierId(supplierId);
    }

    @GetMapping
    public PageResponseDto<ProductResponse> findAll(@PageableDefault(size = 10) Pageable pagination) {
        return productService.findAll(pagination);
    }

    @PutMapping("{productId}")
    public ProductResponse update(@RequestBody @Valid ProductRequest productRequest, @PathVariable UUID productId){
        return productService.update(productRequest, productId);
    }

    @DeleteMapping("{id}")
    public SuccessResponse delete(@PathVariable(name = "id") UUID productId){
        return productService.delete(productId);
    }
}
