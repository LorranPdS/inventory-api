package br.com.potential.supermarket.interfaces;

import br.com.potential.supermarket.dto.request.ProductRequest;
import br.com.potential.supermarket.dto.response.ProductResponse;

public interface ProductService {

    ProductResponse save(ProductRequest productRequest);

}
