package br.com.potential.supermarket.controller;

import br.com.potential.supermarket.dto.request.CategoryRequest;
import br.com.potential.supermarket.dto.response.CategoryResponse;
import br.com.potential.supermarket.dto.PageResponseDto;
import br.com.potential.supermarket.interfaces.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Here are my endpoints related to Category.
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public CategoryResponse save(@RequestBody @Valid CategoryRequest categoryRequest){
        return categoryService.save(categoryRequest);
    }

    @PutMapping("{id}")
    public CategoryResponse update(@RequestBody @Valid CategoryRequest categoryRequest, @PathVariable UUID id){
        return categoryService.update(categoryRequest, id);
    }

    @GetMapping("{id}")
    public CategoryResponse findById(@PathVariable(name = "id") UUID idCategory) { // coloquei aquele name ali no PathVariable pra lembrar dessa possibilidade
        return categoryService.findById(idCategory);
    }

    @GetMapping("code/{code}")
    public CategoryResponse findByCode(@PathVariable @NotNull String code){
        return categoryService.findByCode(code);
    }

    @GetMapping("description/{description}")
    public List<CategoryResponse> findByDescription(@PathVariable String description){
        return categoryService.findByDescription(description);
    }

    @GetMapping
    public PageResponseDto<CategoryResponse> findAll(@PageableDefault(size = 10) Pageable pagination) {
        return categoryService.findAll(pagination);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable UUID id){
        categoryService.delete(id);
    }
}
