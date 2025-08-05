package com.loopers.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

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

        @DisplayName("할인 최소 가격이 null 이면, 생성에 실패한다.")
        @Test
        void fail_whenMinimumOrderAmountIsNull() {
            // act
            var exception = assertThrows(IllegalArgumentException.class, () ->
                Coupon.fixedAmount("쿠폰", "쿠폰 설명", 5000L, null, null)
            );

            // assert
            assertThat(exception.getMessage()).isEqualTo("할인 최소 가격은 필수입니다.");
        }

        @DisplayName("할인 최소 가격이 0 미만이면, 생성에 실패한다.")
        @ParameterizedTest
        @ValueSource(longs = {-1L, -1111L})
        void fail_whenMinimumOrderAmountIsLessThanZero(long minimumOrderAmount) {
            // act
            var exception = assertThrows(IllegalArgumentException.class, () ->
                Coupon.fixedAmount("쿠폰", "쿠폰 설명", 5000L, minimumOrderAmount, null)
            );

            // assert
            assertThat(exception.getMessage()).isEqualTo("할인 최소 가격은 0이상이어야 합니다.");
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
                () -> Coupon.percentage("쿠폰", "쿠폰쿠폰", null, 10000L, 10000L, null)
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
                () -> Coupon.percentage("쿠폰", "쿠폰쿠폰", discountRate, 10000L, 10000L, null)
            );

            // assert
            assertThat(exception.getMessage()).isEqualTo("할인율은 0 보다 커야 합니다.");
        }

        @DisplayName("최대 할인 가격이 null 이면, 생성에 실패한다.")
        @Test
        void failCreatePercentageCoupon_whenMaximumDiscountAmountIsNull() {
            // act
            var exception = assertThrows(IllegalArgumentException.class,
                () -> Coupon.percentage("쿠폰", "쿠폰쿠폰", 10L, 10000L, null, null)
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
                () -> Coupon.percentage("쿠폰", "쿠폰쿠폰", 10L, 10000L, maximumDiscountAmount, null)
            );

            // assert
            assertThat(exception.getMessage()).isEqualTo("최대 할인 가격은 0 보다 커야합니다.");
        }
    }

    @DisplayName("쿠폰을 사용할 때, ")
    @Nested
    class Use {
        @DisplayName("정액 할인 쿠폰인 경우, 최소 주문 금액 보다 주문 가격이 크거나 같으면 허용한다.")
        @Test
        void useCoupon_whenFixedAmountCouponWithValidTotalPrice() {
            // arrange
            Coupon coupon = Coupon.fixedAmount("정액 할인 쿠폰", "쿠폰 설명", 5000L, 20000L, null);

            // act
            UserCoupon userCoupon = coupon.apply(1L, BigDecimal.valueOf(20000), new FixedAmountDiscountStrategy());

            // assert
            assertAll(
                () -> assertThat(userCoupon.getUserId()).isEqualTo(1L),
                () -> assertThat(userCoupon.getDiscountAmount()).isEqualByComparingTo(BigDecimal.valueOf(5000))
            );
        }

        @DisplayName("정액 할인 쿠폰인 경우, 최소 주문 금액 보다 주문 가격이 작으면 예외를 발생한다.")
        @Test
        void useCoupon_whenFixedAmountCouponWithInValidTotalPrice() {
            // arrange
            Coupon coupon = Coupon.fixedAmount("정액 할인 쿠폰", "쿠폰 설명", 5000L, 20000L, null);

            // act
            var exception = assertThrows(IllegalStateException.class, () ->
                coupon.apply(1L, BigDecimal.valueOf(19999), new FixedAmountDiscountStrategy())
            );

            // assert
            assertThat(exception.getMessage()).isEqualTo("적용할 수 없는 쿠폰입니다. 주문 최소 가격: 20000, 현재 가격: 19999");
        }

        @DisplayName("정률 할인 쿠폰인 경우, 최소 주문 금액 보다 주문 가격이 크거나 같으면 허용한다.")
        @Test
        void useCoupon_whenPercentageCouponWithValidTotalPrice() {
            // arrange
            Coupon coupon = Coupon.percentage("정액 할인 쿠폰", "쿠폰 설명", 10L, 20000L, 3000L, null);

            // act
            UserCoupon userCoupon = coupon.apply(1L, BigDecimal.valueOf(20000), new PercentageDiscountStrategy());

            // assert
            assertAll(
                () -> assertThat(userCoupon.getUserId()).isEqualTo(1L),
                () -> assertThat(userCoupon.getDiscountAmount()).isEqualByComparingTo(BigDecimal.valueOf(2000))
            );
        }

        @DisplayName("정액 할인 쿠폰인 경우, 최소 주문 금액 보다 주문 가격이 작으면 예외를 발생한다.")
        @Test
        void useCoupon_whenPercentageCouponWithInValidTotalPrice() {
            // arrange
            Coupon coupon = Coupon.percentage("정액 할인 쿠폰", "쿠폰 설명", 10L, 20000L, 3000L, null);

            // act
            var exception = assertThrows(IllegalStateException.class, () ->
                coupon.apply(1L, BigDecimal.valueOf(19999), new PercentageDiscountStrategy())
            );

            // assert
            assertThat(exception.getMessage()).isEqualTo("적용할 수 없는 쿠폰입니다. 주문 최소 가격: 20000, 현재 가격: 19999");
        }

        @DisplayName("정액 할인 쿠폰인 경우, 할인 가격은 소수점 첫째자리에서 올림한다.")
        @Test
        void useCoupon_whenPercentageCouponWithRoundingUp() {
            // arrange
            Coupon coupon = Coupon.percentage("정액 할인 쿠폰", "쿠폰 설명", 7L, 4000L, 500L, null);

            // act
            UserCoupon userCoupon = coupon.apply(1L, BigDecimal.valueOf(4320), new PercentageDiscountStrategy());

            // assert
            assertThat(userCoupon.getDiscountAmount()).isEqualByComparingTo(BigDecimal.valueOf(303)); // 4320 * 0.07 = 302.4
        }
    }
}
