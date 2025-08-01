package com.loopers.domain.product;

import java.util.List;

public class ProductCommand {
    public record CalculatePrice(
        List<PricingOption> options
    ) {
    }

    public record PricingOption(
        Long productId, Long productOptionId, Integer quantity
    ) {
    }

    public record Search(
        SortBy sortBy,
        int page,
        int size,
        ProductStatus status
    ) {
    }
}
