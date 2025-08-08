package com.loopers.domain.coupon;

import java.math.BigDecimal;

import com.loopers.domain.BaseEntity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private CouponType couponType;
    private Long discountRate;
    private Long discountAmount;
    private Long minimumOrderAmount;
    private Long maximumDiscountAmount;
    private Long limitCount;
    private Long issuedCount;

    private Coupon(String name, String description, CouponType couponType, Long discountRate, Long discountAmount,
            Long minimumOrderAmount, Long maximumDiscountAmount, Long limitCount) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("쿠폰 이름은 비어있을 수 없습니다.");
        }
        if (minimumOrderAmount == null) {
            throw new IllegalArgumentException("할인 최소 가격은 필수입니다.");
        }
        if (minimumOrderAmount < 0) {
            throw new IllegalArgumentException("할인 최소 가격은 0이상이어야 합니다.");
        }
        if (limitCount != null && limitCount <= 0) {
            throw new IllegalArgumentException("사용 한도는 0보다 커야합니다.");
        }

        this.name = name;
        this.description = description;
        this.couponType = couponType;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.minimumOrderAmount = minimumOrderAmount;
        this.maximumDiscountAmount = maximumDiscountAmount;
        this.limitCount = limitCount;
        this.issuedCount = 0L;
    }

    public static Coupon fixedAmount(String name, String description, Long discountAmount, Long minimumOrderAmount,
            @Nullable Long limitCount) {
        if (discountAmount == null) {
            throw new IllegalArgumentException("정액 할인 쿠폰은 할인 가격이 필수입니다.");
        }
        if (discountAmount <= 0) {
            throw new IllegalArgumentException("정액 할인 가격은 0 보다 커야합니다.");
        }
        return new Coupon(name, description, CouponType.FIXED_AMOUNT, null, discountAmount, minimumOrderAmount, null,
                limitCount);
    }

    public static Coupon percentage(String name, String description, Long discountRate, Long minimumOrderAmount,
            Long maximumDiscountAmount,
            @Nullable Long limitCount) {
        if (discountRate == null) {
            throw new IllegalArgumentException("정률 할인 쿠폰은 할인율이 필수 입니다.");
        }
        if (discountRate <= 0) {
            throw new IllegalArgumentException("할인율은 0 보다 커야 합니다.");
        }
        if (maximumDiscountAmount == null) {
            throw new IllegalArgumentException("정률 할인 쿠폰은 최대 할인 가격이 필수입니다.");
        }
        if (maximumDiscountAmount <= 0) {
            throw new IllegalArgumentException("최대 할인 가격은 0 보다 커야합니다.");
        }
        return new Coupon(name, description, CouponType.PERCENTAGE, discountRate, null, minimumOrderAmount,
                maximumDiscountAmount,
                limitCount);
    }

    public CouponUsage apply(Long userId, BigDecimal totalPrice, CouponDiscountStrategy strategy) {
        ++issuedCount;
        if (limitCount != null && limitCount.compareTo(issuedCount) < 0) {
            throw new IllegalStateException("사용한도를 초과한 쿠폰입니다.");
        }
        return new CouponUsage(this, userId, strategy.calculateDiscount(this, totalPrice));
    }
}
