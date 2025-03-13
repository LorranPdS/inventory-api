package br.com.potential.inventory.controller;

import br.com.potential.inventory.dto.CategoryRequest;
import br.com.potential.inventory.dto.CategoryResponse;
import br.com.potential.inventory.dto.PageResponseDto;
import br.com.potential.inventory.interfaces.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public CategoryResponse save(@RequestBody @Valid CategoryRequest categoryRequest){
        return categoryService.save(categoryRequest);
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
}
