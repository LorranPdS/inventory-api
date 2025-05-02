package br.com.potential.supermarket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponseDto<T> {

    private List<T> content;
    private Long totalElements;
    private Integer totalPages;
}
