package br.com.potential.inventory.interfaces;

import br.com.potential.inventory.dto.request.ProductRequest;
import br.com.potential.inventory.dto.response.ProductResponse;

public interface ProductService {

    ProductResponse save(ProductRequest productRequest);

}
