package com.loopers.domain.coupon;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PercentageDiscountStrategy implements CouponDiscountStrategy {
    @Override
    public boolean support(CouponType couponType) {
        return CouponType.PERCENTAGE == couponType;
    }

    @Override
    public BigDecimal calculateDiscount(Coupon coupon, BigDecimal price) {
        if (coupon.getMinimumOrderAmount() > price.intValue()) {
            throw new IllegalStateException(
                String.format("적용할 수 없는 쿠폰입니다. 주문 최소 가격: %s, 현재 가격: %s", coupon.getMinimumOrderAmount(), price));
        }
        return price.multiply(BigDecimal.valueOf(coupon.getDiscountRate()))
            .divide(BigDecimal.valueOf(100), 0, RoundingMode.CEILING);
    }
}
