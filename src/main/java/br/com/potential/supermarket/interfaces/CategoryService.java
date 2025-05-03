package br.com.potential.supermarket.interfaces;

import br.com.potential.supermarket.dto.PageResponseDto;
import br.com.potential.supermarket.dto.request.CategoryRequest;
import br.com.potential.supermarket.dto.response.CategoryResponse;
import br.com.potential.supermarket.entity.CategoryEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryResponse save(CategoryRequest categoryRequest);

    CategoryResponse update(CategoryRequest categoryRequest, UUID id);

    CategoryResponse findById(UUID id);

    CategoryEntity findByIdEntity(UUID id);

    CategoryResponse findByCode(String code);

    List<CategoryResponse> findByDescription(String description);

    PageResponseDto<CategoryResponse> findAll(Pageable pagination);

    void delete(UUID id);
}
