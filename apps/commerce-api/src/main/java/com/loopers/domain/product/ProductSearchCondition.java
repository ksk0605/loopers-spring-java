package com.loopers.domain.product;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductSearchCondition {
    private SortBy sortBy;
    private int page;
    private int size;
    private ProductStatus status;

    public int getOffset() {
        return page * size;
    }

    public int getLimit() {
        return size;
    }
}
