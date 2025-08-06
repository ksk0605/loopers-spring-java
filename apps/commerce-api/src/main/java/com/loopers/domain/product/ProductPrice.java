package com.loopers.domain.product;

import java.math.BigDecimal;

public record ProductPrice(
    Long productId,
    Long productOptionId,
    BigDecimal basePrice,
    BigDecimal optionPrice
) {
}
