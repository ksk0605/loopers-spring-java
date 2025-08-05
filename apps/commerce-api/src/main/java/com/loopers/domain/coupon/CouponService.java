package com.loopers.domain.coupon;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final CouponDiscountStrategyFactory discountStrategyFactory;

    public UserCoupon apply(Long userId, Long couponId, BigDecimal totalPrice) {
        Coupon coupon = couponRepository.find(couponId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[ID = " + couponId + "] 존재하지 않는 쿠폰입니다."));
        if (userCouponRepository.exists(userId, couponId)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "이미 사용한 쿠폰입니다.");
        }
        UserCoupon userCoupon = coupon.apply(userId, totalPrice, discountStrategyFactory.get(coupon));
        return userCouponRepository.save(userCoupon);
    }
}
