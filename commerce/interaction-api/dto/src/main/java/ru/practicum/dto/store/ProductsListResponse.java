package ru.practicum.dto.store;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductsListResponse {
    List<SortProperties> sort;
    List<ProductDto> content;
}

