package com.loopers.domain.inventory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.loopers.infrastructure.inventory.InventoryJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest
class InventoryValidationServiceIntegrationTest {

    @Autowired
    private InventoryValidationService inventoryValidationService;

    @Autowired
    private InventoryJpaRepository inventoryJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("재고를 검증할 때, ")
    @Nested
    class Validate {
        @DisplayName("존재하지 않는 상품 ID를 주면, NOT FOUND 예외를 던진다.")
        @Test
        void throwsException_whenInvalidProductId() {
            // arrange
            Long invalidProductId = 999L;

            // act
            CoreException result = assertThrows(CoreException.class,
                () -> inventoryValidationService.validate(invalidProductId, 1L, 1)
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("재고가 부족하면, CONFLICT 예외를 던진다.")
        @Test
        void throwsException_whenInventoryIsNotEnough() {
            // arrange
            Inventory inventory = new Inventory(1L, 1L, 10);
            inventoryJpaRepository.save(inventory);

            // act
            CoreException result = assertThrows(CoreException.class,
                () -> inventoryValidationService.validate(1L, 1L, 11)
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.CONFLICT);
        }
    }
}
