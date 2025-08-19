package com.loopers.application.order;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.coupon.CouponUsage;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentService;
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
    private final PaymentService paymentService;

    @Transactional
    public OrderResult order(OrderCriteria.Order criteria) {
        List<ProductPrice> productPrices = productService.getAvailableProductPrices(criteria.toProductCommand());
        Order order = orderService.create(criteria.toOrderCommandWithProductPrices(productPrices));

        CouponUsage userCoupon = couponService.apply(criteria.userId(), criteria.couponId(), order.getTotalPrice());
        BigDecimal amount = order.getTotalPrice().subtract(userCoupon.getDiscountAmount());
        paymentService.create(new PaymentCommand.Create(order.getOrderId(), amount));

        return OrderResult.of(order, userCoupon);
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
