package com.loopers.domain.coupon;

public interface CouponUsageRepository {
    CouponUsage save(CouponUsage userCoupon);

    boolean exists(Long userId, Long couponId);
}
