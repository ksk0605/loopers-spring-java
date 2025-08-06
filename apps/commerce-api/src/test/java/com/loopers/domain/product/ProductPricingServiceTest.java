package com.loopers.domain.product;

import static com.loopers.support.fixture.OrderFixture.anOrder;
import static com.loopers.support.fixture.OrderItemFixture.anOrderItem;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.loopers.domain.order.Order;
import com.loopers.infrastructure.order.OrderJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest
public class ProductPricingServiceTest {

    @Autowired
    private ProductPricingService productPricingService;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("총 가격을 계산할 때, ")
    @Nested
    class CalculatePrice {
        @DisplayName("상품 가격과 옵션 가격을 합산하여 반환한다.")
        @Test
        void returnsTotalPrice() {
            // arrange
            Product product = new Product(
                "상품 이름",
                null,
                BigDecimal.valueOf(10000),
                ProductStatus.ON_SALE,
                1L,
                1L,
                LocalDateTime.now().plusDays(1));
            product.addOption(new ProductOption(
                "SIZE",
                "M",
                BigDecimal.valueOf(1000)));
            product.addOption(new ProductOption(
                "SIZE",
                "L",
                BigDecimal.valueOf(2000)));
            product.addOption(new ProductOption(
                "SIZE",
                "XL",
                BigDecimal.valueOf(3000)));
            productJpaRepository.save(product);

            Order order = anOrder()
                .orderItems(
                    List.of(anOrderItem().productOptionId(1L).quantity(1).build(),
                        anOrderItem().productOptionId(2L).quantity(1).build()))
                .build();
            orderJpaRepository.save(order);
            List<ProductCommand.PricingOption> options = order.getItems().stream()
                .map(item -> new ProductCommand.PricingOption(item.getProductId(), item.getProductOptionId(), item.getQuantity()))
                .toList();
            ProductCommand.CalculatePrice command = new ProductCommand.CalculatePrice(options);

            // act
            BigDecimal totalPrice = productPricingService.calculatePrice(command);

            // assert
            assertThat(totalPrice).isEqualByComparingTo(BigDecimal.valueOf(35000));
        }
    }
}
