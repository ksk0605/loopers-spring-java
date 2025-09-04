package com.loopers.domain.inventory;

import static com.loopers.support.fixture.InventoryFixture.anInventory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class InventoryTest {

    @DisplayName("재고를 생성할 때, ")
    @Nested
    class Create {
        @DisplayName("올바른 정보들이 주어지면, 재고를 생성한다.")
        @ParameterizedTest
        @ValueSource(ints = {0, 1, 10, 100})
        void createInventory_whenValidInfoProvided(Integer quantity) {
            // arrange
            Long productId = 1L;
            Long productOptionId = 1L;

            // act
            Inventory inventory = new Inventory(productId, productOptionId, quantity);

            // assert
            assertAll(
                () -> assertThat(inventory.getProductId()).isEqualTo(productId),
                () -> assertThat(inventory.getProductOptionId()).isEqualTo(productOptionId),
                () -> assertThat(inventory.getQuantity()).isEqualTo(quantity)
            );
        }

        @DisplayName("상품 ID가 비어있으면, 예외를 발생시킨다.")
        @Test
        void createInventory_whenProductIdIsEmpty() {
            // act & assert
            assertThrows(IllegalArgumentException.class,
                () -> new Inventory(null, 1L, 1)
            );
        }

        @DisplayName("상품 옵션 ID가 비어있으면, 예외를 발생시킨다.")
        @Test
        void createInventory_whenProductOptionIdIsEmpty() {
            // act & assert
            assertThrows(IllegalArgumentException.class,
                () -> new Inventory(1L, null, 1)
            );
        }

        @DisplayName("재고가 없으면, 예외를 발생시킨다.")
        @Test
        void createInventory_whenQuantityIsEmpty() {
            // act & assert
            assertThrows(IllegalArgumentException.class,
                () -> new Inventory(1L, 1L, null)
            );
        }

        @DisplayName("재고가 0 미만이면, 예외를 발생시킨다.")
        @Test
        void createInventory_whenQuantityIsZero() {
            // arrange
            Long productId = 1L;
            Long productOptionId = 1L;
            Integer quantity = -1;

            // act & assert
            assertThrows(IllegalArgumentException.class,
                () -> new Inventory(productId, productOptionId, quantity)
            );
        }
    }

    @DisplayName("재고를 차감할 때, ")
    @Nested
    class Deduct {
        @DisplayName("보유 재고보다 높은 양을 차감하려고 하면 예외를 발생한다.")
        @Test
        void deduct_failsWhenRequestedQuantityIsGreaterThanAvailableQuantity() {
            // arrange
            Inventory inventory = anInventory().build();

            // act
            var result = assertThrows(IllegalArgumentException.class, () -> inventory.deduct(11));

            // assert
            assertThat(result.getMessage()).isEqualTo("재고가 부족합니다.");
        }
    }

    @DisplayName("재고를 선점할 때, ")
    @Nested
    class Reverse {
        @DisplayName("보유 재고 - 선점 재고 보다 높은 양을 선점하려고 하면 예외가 발생한다.")
        @Test
        void reverse_failsWhenRequestedQuantityIsLessThanAvailableQuantity() {
            // arrange
            Inventory inventory = anInventory().build(); // 10 개
            inventory.reserve(5);

            // act
            var result = assertThrows(IllegalArgumentException.class, () -> inventory.reserve(6));

            // assert
            assertThat(result.getMessage()).isEqualTo("선점할 재고가 부족합니다.");
        }
    }
}
