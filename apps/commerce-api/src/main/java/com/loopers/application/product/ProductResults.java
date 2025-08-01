package com.loopers.application.product;

import java.util.List;

import com.loopers.application.common.PageInfo;

public record ProductResults(
    List<ProductResult> products,
    PageInfo pageInfo
) {
}
