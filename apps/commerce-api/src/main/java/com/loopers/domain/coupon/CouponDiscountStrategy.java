package com.loopers.domain.coupon;

import java.math.BigDecimal;

public interface CouponDiscountStrategy {
    boolean support(CouponType couponType);

    BigDecimal calculateDiscount(Coupon coupon, BigDecimal price);
}
