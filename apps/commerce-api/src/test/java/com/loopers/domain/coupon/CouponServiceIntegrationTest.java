package com.loopers.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.loopers.infrastructure.coupon.CouponJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest
class CouponServiceIntegrationTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("쿠폰을 사용할 때,")
    @Nested
    class Use {
        @DisplayName("올바른 쿠폰 아이디를 주면 쿠폰을 생성한다.")
        @Test
        void useCoupon_whenValidCouponIdIsProvided() {
            // arrange
            Coupon coupon = couponJpaRepository.save(Coupon.fixedAmount("새로운 쿠폰", "쿠폰의 설명입니다.", 5000L, 30000L, null));

            // act
            UserCoupon userCoupon = couponService.apply(1L, coupon.getId(), BigDecimal.valueOf(30000));

            // assert
            assertAll(
                () -> assertThat(userCoupon.getCoupon().getId()).isEqualTo(coupon.getId()),
                () -> assertThat(userCoupon.getDiscountAmount()).isEqualByComparingTo(BigDecimal.valueOf(coupon.getDiscountAmount())),
                () -> assertThat(userCoupon.getUserId()).isEqualTo(1L)
            );
        }

        @DisplayName("존재하지 않는 쿠폰 ID를 주면, NOT FOUND 예외를 발생한다.")
        @Test
        void throwsNotFoundException_whenInvalidCouponIdIsProvided() {
            // act
            var exception = assertThrows(CoreException.class, () ->
                couponService.apply(1L, 1L, BigDecimal.valueOf(30000))
            );

            // assert
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }
    }
}
