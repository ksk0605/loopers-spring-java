package com.loopers.infrastructure.coupon;

import org.springframework.stereotype.Component;

import com.loopers.domain.coupon.CouponUsage;
import com.loopers.domain.coupon.CouponUsageRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CouponUsageCoreRepository implements CouponUsageRepository {
    private final CouponUsageJpaRepository userCouponJpaRepository;

    @Override
    public CouponUsage save(CouponUsage userCoupon) {
        return userCouponJpaRepository.save(userCoupon);
    }

    @Override
    public boolean exists(Long userId, Long couponId) {
        return userCouponJpaRepository.existsByUserIdAndCouponId(userId, couponId);
    }
}
