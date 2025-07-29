package com.loopers.domain.inventory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public class InventoryTest {

    @DisplayName("재고를 생성할 때, ")
    @Nested
    class Create {
        @DisplayName("올바른 정보들이 주어지면, 재고를 생성한다.")
        @Test
        void createInventory_whenValidInfoProvided() {
            // arrange
            Long productId = 1L;
            Long productOptionId = 1L;
            Integer quantity = 10;

            // act
            Inventory inventory = new Inventory(productId, productOptionId, quantity);

            // assert
            assertAll(
                () -> assertThat(inventory.getProductId()).isEqualTo(productId),
                () -> assertThat(inventory.getProductOptionId()).isEqualTo(productOptionId),
                () -> assertThat(inventory.getQuantity()).isEqualTo(quantity)
            );
        }

        @DisplayName("재고가 0 이하면, BAD REQUEST 예외를 발생시킨다.")
        @Test
        void createInventory_whenQuantityIsZero() {
            // arrange
            Long productId = 1L;
            Long productOptionId = 1L;
            Integer quantity = 0;

            // act
            CoreException result = assertThrows(CoreException.class,
                () -> new Inventory(productId, productOptionId, quantity)
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }
}
