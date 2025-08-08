package com.loopers.infrastructure.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loopers.domain.coupon.OptimisticCoupon;

@Repository
public interface OptimisticCouponJpaRepository extends JpaRepository<OptimisticCoupon, Long> {
}
