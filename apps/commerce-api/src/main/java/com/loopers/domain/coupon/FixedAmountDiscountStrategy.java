package com.loopers.domain.coupon;

import java.math.BigDecimal;

public class FixedAmountDiscountStrategy implements CouponDiscountStrategy {

    @Override
    public boolean support(CouponType couponType) {
        return couponType == CouponType.FIXED_AMOUNT;
    }

    @Override
    public BigDecimal calculateDiscount(Coupon coupon, BigDecimal price) {
        if (coupon.getMinimumOrderAmount() > price.intValue()) {
            throw new IllegalStateException(
                String.format("적용할 수 없는 쿠폰입니다. 주문 최소 가격: %s, 현재 가격: %s", coupon.getMinimumOrderAmount(), price));
        }
        return BigDecimal.valueOf(coupon.getDiscountAmount());
    }
}
