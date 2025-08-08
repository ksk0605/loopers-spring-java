package com.loopers.infrastructure.coupon;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.coupon.CouponUsage;

public interface CouponUsageJpaRepository extends JpaRepository<CouponUsage, Long> {
    boolean existsByUserIdAndCouponId(Long userId, Long couponId);
}
