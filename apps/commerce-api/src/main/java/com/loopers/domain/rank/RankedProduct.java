package com.loopers.domain.rank;

public record RankedProduct(
    Long productId,
    Long rank,
    double score
) {
}
