package com.loopers.application.order;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.inventory.InventoryService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.product.ProductPrice;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserService;
import com.loopers.support.annotation.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class OrderUseCase {
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final ProductService productService;
    private final InventoryService inventoryService;
    private final UserService userService;

    @Transactional
    public OrderResult order(OrderCriteria.Order criteria) {
        List<ProductPrice> productPrices = productService.getAvailableProductPrices(criteria.toProductCommand());

        Order order = orderService.order(criteria.toOrderCommandWithProductPrices(productPrices));

        PaymentCommand.Pay command = new PaymentCommand.Pay(order.getId(), PaymentMethod.POINT, order.getTotalPrice());
        Payment payment = paymentService.pay(command);
        order.pay();

        inventoryService.deduct(criteria.toInventoryCommand());

        userService.pay(criteria.userId(), order.getTotalPrice());

        return OrderResult.of(order, payment);
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
