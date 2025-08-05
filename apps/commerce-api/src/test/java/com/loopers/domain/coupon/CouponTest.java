package com.loopers.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CouponTest {

    @DisplayName("쿠폰을 생성할 때, ")
    @Nested
    class Create {
        @DisplayName("이름이 null 이면, 생성에 실패한다. ")
        @Test
        void fail_whenNameIsNull() {
            // act
            var exception = assertThrows(IllegalArgumentException.class, () ->
                Coupon.fixedAmount(null, "쿠폰 설명", 5000L, 20000L, null)
            );

            // assert
            assertThat(exception.getMessage()).isEqualTo("쿠폰 이름은 비어있을 수 없습니다.");
        }

        @DisplayName("이름이 비어있으면, 생성에 실패한다.")
        @Test
        void fail_whenNameIsBlank() {
            // act
            var exception = assertThrows(IllegalArgumentException.class, () ->
                Coupon.fixedAmount("", "쿠폰 설명", 5000L, 20000L, null)
            );

            // assert
            assertThat(exception.getMessage()).isEqualTo("쿠폰 이름은 비어있을 수 없습니다.");
        }
    }

    @DisplayName("정액 쿠폰을 생성할 때, ")
    @Nested
    class CreateFixedAmountCoupon {

        @DisplayName("할인 가격이 없으면, 생성에 실패한다.")
        @Test
        void failCreateFixedCoupon_whenDiscountAmountIsNull() {
            // act
            var exception = assertThrows(IllegalArgumentException.class,
                () -> Coupon.fixedAmount("쿠폰", "쿠폰쿠폰", null, null, null)
            );

            // assert
            assertThat(exception.getMessage()).isEqualTo("정액 할인 쿠폰은 할인 가격이 필수입니다.");
        }

        @DisplayName("할인 가격이 0이하면, 생성에 실패한다.")
        @ParameterizedTest
        @ValueSource(longs = {0L, -1L})
        void failCreateFixedCoupon_whenDiscountAmountIsZeroOrLess(long discountAmount) {
            // act
            var exception = assertThrows(IllegalArgumentException.class,
                () -> Coupon.fixedAmount("쿠폰", "쿠폰쿠폰", discountAmount, null, null)
            );

            // assert
            assertThat(exception.getMessage()).isEqualTo("정액 할인 가격은 0 보다 커야합니다.");
        }

        @DisplayName("할인 최소 가격이 null 이면, 생성에 실패한다.")
        @Test
        void failCreateFixedCoupon_whenMinimumDiscountAmountIsNull() {
            // act
            var exception = assertThrows(IllegalArgumentException.class,
                () -> Coupon.fixedAmount("쿠폰", "쿠폰쿠폰", 1000L, null, null)
            );

            // assert
            assertThat(exception.getMessage()).isEqualTo("정액 할인 쿠폰은 할인 최소 가격이 필수입니다.");
        }

        @DisplayName("할인 최소 가격이 0보다 작으면, 생성에 실패한다.")
        @ParameterizedTest
        @ValueSource(longs = {-1L, -1111L})
        void failCreateFixedCoupon_whenMinimumDiscountAmountIsLessThanZero(long minimumDiscountAmount) {
            // act
            var exception = assertThrows(IllegalArgumentException.class,
                () -> Coupon.fixedAmount("쿠폰", "쿠폰쿠폰", 1000L, minimumDiscountAmount, null)
            );

            // assert
            assertThat(exception.getMessage()).isEqualTo("할인 최소 가격은 0이상이어야 합니다.");
        }
    }

    @DisplayName("정률 할인 쿠폰을 생성할 때, ")
    @Nested
    class CreatePercentageCoupon {
        @DisplayName("할인율이 null 이면, 생성에 실패한다.")
        @Test
        void failCreatePercentageCoupon_whenDiscountRateIsNull() {
            // act
            var exception = assertThrows(IllegalArgumentException.class,
                () -> Coupon.percentage("쿠폰", "쿠폰쿠폰", null, 10000L, null)
            );

            // assert
            assertThat(exception.getMessage()).isEqualTo("정률 할인 쿠폰은 할인율이 필수 입니다.");
        }

        @DisplayName("할인율이 0 이하 이면, 생성에 실패한다.")
        @ParameterizedTest
        @ValueSource(longs = {-1L, -1111L})
        void failCreatePercentageCoupon_whenDiscountRateIsZeroOrLess(long discountRate) {
            // act
            var exception = assertThrows(IllegalArgumentException.class,
                () -> Coupon.percentage("쿠폰", "쿠폰쿠폰", discountRate, 10000L, null)
            );

            // assert
            assertThat(exception.getMessage()).isEqualTo("할인율은 0 보다 커야 합니다.");
        }

        @DisplayName("최대 할인 가격이 null 이면, 생성에 실패한다.")
        @Test
        void failCreatePercentageCoupon_whenMaximumDiscountAmountIsNull() {
            // act
            var exception = assertThrows(IllegalArgumentException.class,
                () -> Coupon.percentage("쿠폰", "쿠폰쿠폰", 10L, null, null)
            );

            // assert
            assertThat(exception.getMessage()).isEqualTo("정률 할인 쿠폰은 최대 할인 가격이 필수입니다.");
        }

        @DisplayName("최대 할인 가격이 0 이하이면, 생성에 실패한다.")
        @ParameterizedTest
        @ValueSource(longs = {-1L, -1111L})
        void failCreatePercentageCoupon_whenMaximumDiscountAmountIsZeroOrLess(long maximumDiscountAmount) {
            // act
            var exception = assertThrows(IllegalArgumentException.class,
                () -> Coupon.percentage("쿠폰", "쿠폰쿠폰", 10L, maximumDiscountAmount, null)
            );

            // assert
            assertThat(exception.getMessage()).isEqualTo("최대 할인 가격은 0 보다 커야합니다.");
        }
    }
}
