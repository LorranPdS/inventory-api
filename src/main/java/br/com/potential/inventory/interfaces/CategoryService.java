package br.com.potential.inventory.interfaces;

import br.com.potential.inventory.dto.CategoryRequest;
import br.com.potential.inventory.dto.CategoryResponse;

public interface CategoryService {

    CategoryResponse save(CategoryRequest categoryRequest);
}
