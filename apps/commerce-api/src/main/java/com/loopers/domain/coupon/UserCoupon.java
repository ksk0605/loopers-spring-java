package com.loopers.domain.coupon;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class UserCoupon {
    private Coupon coupon;
    private Long userId;
    private BigDecimal discountAmount;

    public UserCoupon(Coupon coupon, Long userId, BigDecimal discountAmount) {
        this.coupon = coupon;
        this.userId = userId;
        this.discountAmount = discountAmount;
    }
}
