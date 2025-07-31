package com.loopers.domain.inventory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

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

        @DisplayName("상품 ID가 비어있으면, BAD REQUEST 예외를 발생시킨다.")
        @Test
        void createInventory_whenProductIdIsEmpty() {
            // act
            CoreException result = assertThrows(CoreException.class,
                () -> new Inventory(null, 1L, 1)
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("상품 옵션 ID가 비어있으면, BAD REQUEST 예외를 발생시킨다.")
        @Test
        void createInventory_whenProductOptionIdIsEmpty() {
            // act
            CoreException result = assertThrows(CoreException.class,
                () -> new Inventory(1L, null, 1)
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("재고가 0 미만이면, BAD REQUEST 예외를 발생시킨다.")
        @Test
        void createInventory_whenQuantityIsZero() {
            // arrange
            Long productId = 1L;
            Long productOptionId = 1L;
            Integer quantity = -1;

            // act
            CoreException result = assertThrows(CoreException.class,
                () -> new Inventory(productId, productOptionId, quantity)
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }
}
