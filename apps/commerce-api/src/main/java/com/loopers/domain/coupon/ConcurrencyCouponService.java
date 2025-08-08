package com.loopers.domain.coupon;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.infrastructure.coupon.OptimisticCouponJpaRepository;
import com.loopers.infrastructure.coupon.PessimisticCouponJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConcurrencyCouponService {
    private final OptimisticCouponJpaRepository optimisticCouponRepository;
    private final PessimisticCouponJpaRepository pessimisticCouponJpaRepository;

    @Retryable(
        value = OptimisticLockingFailureException.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 50, maxDelay = 500, random = true)
    )
    @Transactional
    public void useOptCoupon(Long id) {
        OptimisticCoupon coupon = optimisticCouponRepository.findById(id).orElseThrow();
        coupon.issue();
        optimisticCouponRepository.save(coupon);
    }

    @Transactional
    public void usePessCoupon(Long id) {
        PessimisticCoupon coupon = pessimisticCouponJpaRepository.findByIdForUpdate(id).orElseThrow();
        coupon.issue();
        pessimisticCouponJpaRepository.save(coupon);
    }
}
