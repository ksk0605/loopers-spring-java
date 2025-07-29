package com.loopers.domain.product;

import org.springframework.data.domain.Page;

public interface ProductViewRepository {
    Page<ProductView> findProducts(ProductSearchCondition condition);
}
