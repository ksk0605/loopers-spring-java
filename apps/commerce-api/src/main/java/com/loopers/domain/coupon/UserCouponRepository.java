package com.loopers.domain.coupon;

public interface UserCouponRepository {
    UserCoupon save(UserCoupon userCoupon);

    boolean exists(Long userId, Long couponId);
}
