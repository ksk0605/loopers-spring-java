package com.loopers.application.order;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.coupon.UserCoupon;
import com.loopers.domain.inventory.InventoryService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.product.ProductPrice;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import com.loopers.support.annotation.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class OrderFacade {
    private final OrderService orderService;
    private final CouponService couponService;
    private final PaymentService paymentService;
    private final ProductService productService;
    private final InventoryService inventoryService;
    private final UserService userService;

    @Transactional
    public OrderResult order(OrderCriteria.Order criteria) {
        User user = userService.get(criteria.userId());

        List<ProductPrice> productPrices = productService.getAvailableProductPrices(criteria.toProductCommand());
        Order order = orderService.create(criteria.toOrderCommandWithProductPrices(productPrices));

        UserCoupon userCoupon = couponService.apply(criteria.userId(), criteria.couponId(), order.getTotalPrice());
        BigDecimal discountPrice = order.getTotalPrice().subtract(userCoupon.getDiscountAmount());

        PaymentCommand.Pay command = new PaymentCommand.Pay(order.getId(), PaymentMethod.POINT, discountPrice);
        Payment payment = paymentService.create(command);
        order.pay();

        inventoryService.deduct(criteria.toInventoryCommand());
        user.usePoint(order.getTotalPrice().intValue());
        return OrderResult.of(order, payment, userCoupon);
    }

    @Transactional(readOnly = true)
    public OrderResult getOrder(Long orderId, Long userId) {
        Order order = orderService.get(orderId, userId);
        Payment payment = paymentService.getByOrderId(order.getId());
        return OrderResult.of(order, payment);
    }

    @Transactional(readOnly = true)
    public OrderResults getOrders(Long userId) {
        List<Order> orders = orderService.getAll(userId);
        List<Long> orderIds = orders.stream().map(order -> order.getId()).toList();
        List<Payment> payments = paymentService.getAll(orderIds);
        return OrderResults.of(orders, payments);
    }
}
