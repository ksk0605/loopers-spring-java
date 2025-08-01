package com.loopers.domain.product;

import java.math.BigDecimal;

import com.loopers.domain.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "product_option")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption extends BaseEntity {
    private String optionType;
    private String optionValue;
    private BigDecimal additionalPrice;

    public ProductOption(String optionType, String optionValue, BigDecimal additionalPrice) {
        this.optionType = optionType;
        this.optionValue = optionValue;
        this.additionalPrice = additionalPrice;
    }
}
