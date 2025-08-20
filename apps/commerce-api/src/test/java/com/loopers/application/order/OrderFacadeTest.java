package com.loopers.application.order;

import static com.loopers.support.fixture.InventoryFixture.anInventory;
import static com.loopers.support.fixture.ProductFixture.aProduct;
import static com.loopers.support.fixture.ProductOptionFixture.aProductOption;
import static com.loopers.support.fixture.UserFixture.anUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.inventory.Inventory;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductOption;
import com.loopers.domain.user.User;
import com.loopers.infrastructure.coupon.CouponJpaRepository;
import com.loopers.infrastructure.coupon.CouponUsageJpaRepository;
import com.loopers.infrastructure.inventory.InventoryJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.IntegrationTest;

class OrderFacadeTest extends IntegrationTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private InventoryJpaRepository inventoryJpaRepository;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private CouponUsageJpaRepository couponUsageJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @DisplayName("주문 정보가 정상적으로 주어지면, 주문 결과를 반환한다.")
    @Test
    void order() {
        // arrange
        User user = anUser().build();
        user.updatePoint(1000000);
        userJpaRepository.save(user);

        Product product = aProduct().name("상품 1").build();
        ProductOption productOption = aProductOption().build();
        product.addOption(productOption);
        productJpaRepository.save(product);

        Inventory inventory = anInventory().build();
        inventoryJpaRepository.save(inventory);

        Coupon coupon = Coupon.fixedAmount("고정 할인", null, 5000L, 10000L, null);
        couponJpaRepository.save(coupon);

        // act
        OrderCriteria.Order cri = new OrderCriteria.Order(1L, user.getUserId(), List.of(new OrderCriteria.Item(1L, 1L, 10)), 1L);
        OrderResult result = orderFacade.order(cri);

        // assert
        assertAll(
            () -> assertThat(result.userId()).isEqualTo(1L),
            () -> assertThat(result.totalPrice()).isEqualByComparingTo(BigDecimal.valueOf(205000)), // (기본20000원 + 옵션 추가1000원) * 10개 - 5000
            () -> assertThat(result.status()).isEqualTo(OrderStatus.PENDING_PAYMENT),
            () -> assertThat(result.id()).isEqualTo(1L)
        );
    }
}
