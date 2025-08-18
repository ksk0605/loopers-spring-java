package com.loopers.support.fixture;

import java.math.BigDecimal;

import com.loopers.domain.product.ProductOption;

public class ProductOptionFixture {
    private String optionType = "SIZE";
    private String optionValue = "M";
    private BigDecimal additionalPrice = BigDecimal.valueOf(1000);

    public static ProductOptionFixture aProductOption() {
        return new ProductOptionFixture();
    }

    public ProductOptionFixture optionType(String optionType) {
        this.optionType = optionType;
        return this;
    }

    public ProductOptionFixture optionValue(String optionValue) {
        this.optionValue = optionValue;
        return this;
    }

    public ProductOptionFixture additionalPrice(BigDecimal additionalPrice) {
        this.additionalPrice = additionalPrice;
        return this;
    }

    public ProductOption build() {
        return new ProductOption(
            optionType,
            optionValue,
            additionalPrice
        );
    }

    private ProductOptionFixture() {
    }
}
