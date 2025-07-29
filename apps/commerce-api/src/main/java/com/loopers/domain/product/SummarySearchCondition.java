package com.loopers.domain.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SummarySearchCondition {
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
