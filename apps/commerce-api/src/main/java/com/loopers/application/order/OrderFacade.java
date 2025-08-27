package com.loopers.application.order;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderCreatedEvent;
import com.loopers.domain.order.OrderEventPublisher;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.product.ProductPrice;
import com.loopers.domain.product.ProductService;
import com.loopers.support.annotation.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class OrderFacade {
    private final OrderService orderService;
    private final CouponService couponService;
    private final ProductService productService;
    private final OrderEventPublisher orderEventPublisher;

    @Transactional
    public OrderResult order(OrderCriteria.Order criteria) {
        List<ProductPrice> productPrices = productService.getAvailableProductPrices(criteria.toProductCommand());
        Order order = orderService.create(criteria.toOrderCommandWithProductPrices(productPrices));
        BigDecimal discountAmount = couponService.calculateDiscountAmount(criteria.couponId(), order.getTotalPrice());
        order.applyDiscount(discountAmount);
        orderEventPublisher.publishOrderCreatedEvent(new OrderCreatedEvent(order, criteria.couponId()));
        return OrderResult.of(order);
    }

    @Transactional(readOnly = true)
    public OrderResult getOrder(Long orderId, Long userId) {
        Order order = orderService.get(orderId, userId);
        return OrderResult.of(order);
    }

    @Transactional(readOnly = true)
    public OrderResults getOrders(Long userId) {
        List<Order> orders = orderService.getAll(userId);
        return OrderResults.of(orders);
    }
}
