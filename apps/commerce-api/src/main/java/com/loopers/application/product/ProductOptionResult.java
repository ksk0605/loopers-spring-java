package com.loopers.application.product;

import com.loopers.domain.product.ProductOptionInfo;

public record ProductOptionResult(
    Long id,
    String optionType,
    String optionValue,
    Long additionalPrice,
    Integer stockQuantity
) {
    public static ProductOptionResult of(ProductOptionInfo productOption, Integer stockQuantity) {
        return new ProductOptionResult(
            productOption.getId(),
            productOption.getOptionType(),
            productOption.getOptionValue(),
            productOption.getAdditionalPrice().longValue(),
            stockQuantity
        );
    }
}
