package com.loopers.domain.product;

import org.springframework.data.domain.Page;

public interface ProductSummaryRepository {
    Page<ProductSummary> findAll(SummarySearchCondition condition);
}
