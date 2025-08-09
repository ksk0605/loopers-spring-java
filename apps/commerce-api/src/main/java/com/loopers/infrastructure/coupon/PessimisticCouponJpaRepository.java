package com.loopers.infrastructure.coupon;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.loopers.domain.coupon.PessimisticCoupon;

import jakarta.persistence.LockModeType;

@Repository
public interface PessimisticCouponJpaRepository extends JpaRepository<PessimisticCoupon, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from PessimisticCoupon c where c.id = :id")
    Optional<PessimisticCoupon> findByIdForUpdate(Long id);
}
