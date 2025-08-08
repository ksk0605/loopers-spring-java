package com.loopers.domain.coupon;

import com.loopers.domain.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pessimistic_coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PessimisticCoupon extends BaseEntity {
    private String name;
    private Long discountAmount;
    private Long limitCount;
    private Long issuedCount;

    public PessimisticCoupon(String name, Long discountAmount, Long limitCount) {
        this.name = name;
        this.discountAmount = discountAmount;
        this.limitCount = limitCount;
        this.issuedCount = 0L;
    }

    public void issue() {
        issuedCount++;
        if (limitCount != null && limitCount.compareTo(issuedCount) < 0) {
            throw new IllegalStateException("사용한도를 초과한 쿠폰입니다.");
        }
    }
}
