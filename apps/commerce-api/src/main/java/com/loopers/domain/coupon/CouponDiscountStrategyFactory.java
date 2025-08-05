package com.loopers.domain.coupon;

import java.util.List;

import org.springframework.stereotype.Component;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CouponDiscountStrategyFactory {
    private final List<CouponDiscountStrategy> discountStrategy;

    public CouponDiscountStrategy get(Coupon coupon) {
        return discountStrategy.stream()
            .filter(strategy -> strategy.support(coupon.getCouponType()))
            .findFirst()
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "쿠폰에 해당하는 할인 전략이 없습니다."));
    }
}
