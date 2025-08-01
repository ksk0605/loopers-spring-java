package com.loopers.domain.product;

import java.math.BigDecimal;

public record ProductOptionInfo(
    String optionType,
    String optionValue,
    BigDecimal additionalPrice
) {
    public static ProductOptionInfo from(ProductOption productOption) {
        return new ProductOptionInfo(
            productOption.getOptionType(),
            productOption.getOptionValue(),
            productOption.getAdditionalPrice());
    }
}
