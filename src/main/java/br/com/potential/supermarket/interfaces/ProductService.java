package br.com.potential.supermarket.interfaces;

import br.com.potential.supermarket.dto.PageResponseDto;
import br.com.potential.supermarket.dto.request.ProductRequest;
import br.com.potential.supermarket.dto.response.ProductResponse;
import br.com.potential.supermarket.dto.response.SuccessResponse;
import br.com.potential.supermarket.entity.ProductEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponse save(ProductRequest productRequest);

    ProductResponse update(ProductRequest productRequest, UUID id);

    ProductResponse findById(UUID id);

    ProductEntity findByIdEntity(UUID id);

    List<ProductResponse> findByName(String name);

    List<ProductResponse> findByCategoryId(UUID categoryId);

    List<ProductResponse> findBySupplierId(UUID supplierId);

    PageResponseDto<ProductResponse> findAll(Pageable pagination);

    Boolean existsByCategoryId(UUID categoryId);

    Boolean existsBySupplierId(UUID supplierId);

    SuccessResponse delete(UUID id);

}
