package com.loopers.domain.coupon;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponUsageRepository couponUsageRepository;
    private final CouponDiscountStrategyFactory discountStrategyFactory;

    @Transactional
    public CouponUsage apply(Long userId, Long couponId, BigDecimal totalPrice) {
        Coupon coupon = couponRepository.findForUpdate(couponId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[ID = " + couponId + "] 존재하지 않는 쿠폰입니다."));
        if (couponUsageRepository.exists(userId, couponId)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "이미 사용한 쿠폰입니다.");
        }
        CouponUsage userCoupon = coupon.apply(userId, totalPrice, discountStrategyFactory.get(coupon));
        return couponUsageRepository.save(userCoupon);
    }
}
