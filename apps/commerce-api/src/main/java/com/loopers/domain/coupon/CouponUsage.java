package com.loopers.domain.coupon;

import java.math.BigDecimal;

import com.loopers.domain.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "coupon_usage", uniqueConstraints = {
        @UniqueConstraint(name = "uk_coupon_usage", columnNames = { "user_id", "coupon_id" })
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponUsage extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Coupon coupon;
    private Long userId;
    private BigDecimal discountAmount;

    public CouponUsage(Coupon coupon, Long userId, BigDecimal discountAmount) {
        this.coupon = coupon;
        this.userId = userId;
        this.discountAmount = discountAmount;
    }
}
