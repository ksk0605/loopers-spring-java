package com.loopers.domain.product;

import java.util.List;

public class ProductCommand {
    public record GetAvailable(
        List<OrderOption> options
    ) {
    }

    public record OrderOption(
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
