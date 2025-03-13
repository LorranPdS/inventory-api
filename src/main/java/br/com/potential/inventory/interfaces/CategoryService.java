package br.com.potential.inventory.interfaces;

import br.com.potential.inventory.dto.CategoryRequest;
import br.com.potential.inventory.dto.CategoryResponse;
import br.com.potential.inventory.dto.PageResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryResponse save(CategoryRequest categoryRequest);

    CategoryResponse findById(UUID id);

    CategoryResponse findByCode(String code);

    List<CategoryResponse> findByDescription(String description);

    PageResponseDto<CategoryResponse> findAll(Pageable pagination);
}
