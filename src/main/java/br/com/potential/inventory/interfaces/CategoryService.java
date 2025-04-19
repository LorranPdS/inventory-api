package br.com.potential.inventory.interfaces;

import br.com.potential.inventory.dto.request.CategoryRequest;
import br.com.potential.inventory.dto.response.CategoryResponse;
import br.com.potential.inventory.dto.PageResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryResponse save(CategoryRequest categoryRequest);

    CategoryResponse update(CategoryRequest categoryRequest, UUID id);

    CategoryResponse findById(UUID id);

    CategoryResponse findByCode(String code);

    List<CategoryResponse> findByDescription(String description);

    PageResponseDto<CategoryResponse> findAll(Pageable pagination);

    void delete(UUID id);
}
