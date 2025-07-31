package com.loopers.application.order;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderItem;
import com.loopers.domain.order.OrderPricingService;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderFacade {
    private final OrderService orderService;
    private final OrderPricingService orderPricingService;
    private final PaymentService paymentService;

    @Transactional
    public OrderInfo placeOrder(Long userId, List<OrderItem> items) {
        Order order = orderService.place(userId, items);
        BigDecimal totalPrice = orderPricingService.calculatePrice(order);
        Payment payment = paymentService.process(order.getId(), PaymentMethod.POINT, totalPrice);
        order.pay();
        return OrderInfo.of(order, payment, totalPrice);
    }
}
