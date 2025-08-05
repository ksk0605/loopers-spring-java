package com.loopers.domain.coupon;

import lombok.Getter;

@Getter
public class Coupon {
    private String name;
    private String description;
    private CouponType couponType;
    private Long discountRate;
    private Long discountAmount;
    private Long minimumOrderAmount;
    private Long maximumDiscountAmount;
    private Integer limitCount;
    private Integer usedCount;

    private Coupon(String name, String description, CouponType couponType, Long discountRate, Long discountAmount,
        Long minimumOrderAmount, Long maximumDiscountAmount, Integer limitCount, Integer usedCount) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("쿠폰 이름은 비어있을 수 없습니다.");
        }

        this.name = name;
        this.description = description;
        this.couponType = couponType;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.minimumOrderAmount = minimumOrderAmount;
        this.maximumDiscountAmount = maximumDiscountAmount;
        this.limitCount = limitCount;
        this.usedCount = usedCount;
    }

    public static Coupon fixedAmount(String name, String description, Long discountAmount, Long minimumOrderAmount,
        Integer limitCount) {
        if (discountAmount == null) {
            throw new IllegalArgumentException("정액 할인 쿠폰은 할인 가격이 필수입니다.");
        }
        if (discountAmount <= 0) {
            throw new IllegalArgumentException("정액 할인 가격은 0 보다 커야합니다.");
        }
        if (minimumOrderAmount == null) {
            throw new IllegalArgumentException("정액 할인 쿠폰은 할인 최소 가격이 필수입니다.");
        }
        if (minimumOrderAmount < 0) {
            throw new IllegalArgumentException("할인 최소 가격은 0이상이어야 합니다.");
        }
        return new Coupon(name, description, CouponType.FIXED_AMOUNT, null, discountAmount, minimumOrderAmount, null, limitCount,
            0);
    }

    public static Coupon percentage(String name, String description, Long discountRate, Long maximumDiscountAmount,
        Integer limitCount) {
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
        return new Coupon(name, description, CouponType.PERCENTAGE, discountRate, null, null, maximumDiscountAmount, limitCount,
            0);
    }
}
