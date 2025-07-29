package com.loopers.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.loopers.domain.inventory.Inventory;
import com.loopers.domain.inventory.InventoryRepository;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.ProductStatus;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public class OrderValidatorTest {
    private OrderValidator validator;
    private ProductRepository productRepository;
    private InventoryRepository inventoryRepository;

    @BeforeEach
    public void setUp() {
        productRepository = mock(ProductRepository.class);
        inventoryRepository = mock(InventoryRepository.class);
        validator = new OrderValidator(productRepository, inventoryRepository);
    }

    @DisplayName("주문을 처리할 때")
    @Nested
    class Validate {
        @DisplayName("주문 항목이 비어있으면, BAD REQUEST 예외를 발생시킨다.")
        @Test
        void validateOrder_whenItemsAreEmpty() {
            // arrange
            Order order = new Order(1L, List.of());

            // act
            CoreException result = assertThrows(CoreException.class,
                () -> validator.validateOrder(order)
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("존재하지 않는 상품을 주문하려고 하면, NOT FOUND 예외를 발생시킨다.")
        @Test
        void validateOrder_whenProductIsNotFound() {
            // arrange
            when(productRepository.find(anyLong())).thenReturn(Optional.empty());
            Order order = new Order(1L, List.of(new OrderItem(1L, 1L, 1)));

            // act
            CoreException result = assertThrows(CoreException.class,
                () -> validator.validateOrder(order)
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("상품이 판매 중이 아니면, CONFLICT 예외를 발생시킨다.")
        @Test
        void validateOrder_whenProductIsNotAvailable() {
            // arrange
            Product product = new Product(
                "상품",
                null,
                BigDecimal.valueOf(10000),
                ProductStatus.SOLD_OUT,
                1L,
                1L,
                LocalDateTime.now().plusDays(1)
            );
            when(productRepository.find(1L)).thenReturn(Optional.of(product));
            Order order = new Order(1L, List.of(new OrderItem(1L, 1L, 1)));

            // act
            CoreException result = assertThrows(CoreException.class,
                () -> validator.validateOrder(order)
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.CONFLICT);
        }

        @DisplayName("주문 수량이 0 이하면, BAD REQUEST 예외를 발생시킨다.")
        @Test
        void validateOrder_whenQuantityIsZero() {
            // arrange
            Order order = new Order(1L, List.of(new OrderItem(1L, 1L, 0)));

            // act
            CoreException result = assertThrows(CoreException.class,
                () -> validator.validateOrder(order)
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("재고가 부족하면, CONFLICT 예외를 발생시킨다.")
        @Test
        void validateOrder_whenInventoryIsNotEnough() {
            // arrange
            Product product = new Product(
                "상품",
                null,
                BigDecimal.valueOf(10000),
                ProductStatus.SOLD_OUT,
                1L,
                1L,
                LocalDateTime.now().plusDays(1)
            );
            Inventory inventory = new Inventory(1L, 1L, 10);
            when(productRepository.find(1L)).thenReturn(Optional.of(product));
            when(inventoryRepository.find(1L, 1L)).thenReturn(Optional.of(inventory));
            Order order = new Order(1L, List.of(new OrderItem(1L, 1L, 11)));

            // act
            CoreException result = assertThrows(CoreException.class,
                () -> validator.validateOrder(order)
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.CONFLICT);
        }
    }
}
