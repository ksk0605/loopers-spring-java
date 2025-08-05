package com.loopers.infrastructure.coupon;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.coupon.UserCoupon;

public interface UserCouponJpaRepository extends JpaRepository<UserCoupon, Long> {
}
