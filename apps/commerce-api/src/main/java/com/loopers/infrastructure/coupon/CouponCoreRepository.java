package com.loopers.infrastructure.coupon;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CouponCoreRepository implements CouponRepository {
    private final CouponJpaRepository couponJpaRepository;

    @Override
    public Coupon save(Coupon coupon) {
        return couponJpaRepository.save(coupon);
    }

    @Override
    public Optional<Coupon> find(Long id) {
        return couponJpaRepository.findById(id);
    }
}
