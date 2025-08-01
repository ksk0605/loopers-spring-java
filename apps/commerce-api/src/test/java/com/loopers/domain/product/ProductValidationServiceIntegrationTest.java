package com.loopers.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest
class ProductValidationServiceIntegrationTest {

    @Autowired
    private ProductValidationService productValidationService;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("상품을 검증할 때 , ")
    @Nested
    class Validate {
        @DisplayName("존재하지 않는 상품 ID를 주면, NOT FOUND 예외를 던진다.")
        @Test
        void throwsException_whenInvalidProductId() {
            // arrange
            Long invalidProductId = 999L;

            // act 
            CoreException result = assertThrows(CoreException.class,
                () -> productValidationService.validate(invalidProductId)
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }
    }

    @DisplayName("판매 중이지 않은 상품 ID를 주면, CONFLICT 예외를 던진다.")
    @Test
    void throwsException_whenProductIsNotOnSale() {
        // arrange
        Product product = new Product(
            "상품 이름",
            null,
            BigDecimal.valueOf(10000),
            ProductStatus.SOLD_OUT,
            1L,
            1L,
            LocalDateTime.now().plusDays(1));
        productJpaRepository.save(product);

        // act
        CoreException result = assertThrows(CoreException.class,
            () -> productValidationService.validate(1L)
        );

        // assert
        assertThat(result.getErrorType()).isEqualTo(ErrorType.CONFLICT);
    }
}
