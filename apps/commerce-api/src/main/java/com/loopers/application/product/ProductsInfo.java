package com.loopers.application.product;

import java.util.List;

import com.loopers.application.common.PageInfo;
import com.loopers.domain.product.ProductSummary;

public record ProductsInfo(
    List<ProductInfo> products,
    PageInfo pageInfo
) {
    public static ProductsInfo of(List<ProductSummary> summaries, PageInfo pageInfo) {
        return new ProductsInfo(
            summaries.stream()
                .map(ProductInfo::from)
                .toList(),
            pageInfo
        );
    }
}
