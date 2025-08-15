package com.loopers.domain.product;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOptionInfo {
    Long id;
    String optionType;
    String optionValue;
    BigDecimal additionalPrice;

    public static ProductOptionInfo from(ProductOption productOption) {
        return new ProductOptionInfo(
            productOption.getId(),
            productOption.getOptionType(),
            productOption.getOptionValue(),
            productOption.getAdditionalPrice()
        );
    }
}
