package com.loopers.application.order;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.inventory.InventoryValidationService;
import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductPricingService;
import com.loopers.domain.product.ProductValidationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderFacade {
    private final ProductValidationService productValidationService;
    private final InventoryValidationService inventoryValidationService;
    private final OrderService orderService;
    private final ProductPricingService productPricingService;
    private final PaymentService paymentService;

    @Transactional
    public OrderResult placeOrder(OrderCommand.Place command) {
        command.options().forEach(option -> {
            productValidationService.validate(option.productId());
            inventoryValidationService.validate(option.productId(), option.productOptionId(), option.quantity());
        });

        var orderInfo = orderService.place(command);

        var options = orderInfo.items().stream()
            .map(item -> new ProductCommand.PricingOption(
                item.productId(),
                item.productOptionId(),
                item.quantity()))
            .toList();
        var productCommand = new ProductCommand.CalculatePrice(options);
        var totalPrice = productPricingService.calculatePrice(productCommand);

        var paymentCommand = new PaymentCommand.Process(orderInfo.id(), PaymentMethod.POINT, totalPrice);
        var paymentInfo = paymentService.process(paymentCommand);

        orderInfo = orderService.pay(orderInfo.id());
        return OrderResult.of(orderInfo, paymentInfo, totalPrice);
    }

    public List<OrderResult> getOrders(Long userId) {
        var orderInfos = orderService.getAll(userId);
        return orderInfos.stream()
            .map(orderInfo -> {
                var options = orderInfo.items().stream()
                    .map(item -> new ProductCommand.PricingOption(
                        item.productId(),
                        item.productOptionId(),
                        item.quantity()))
                    .toList();
                var productCommand = new ProductCommand.CalculatePrice(options);
                var totalPrice = productPricingService.calculatePrice(productCommand);
                var paymentInfo = paymentService.get(orderInfo.id());
                return OrderResult.of(orderInfo, paymentInfo, totalPrice);
            })
            .toList();
    }

    public OrderResult getOrder(Long orderId, Long userId) {
        var orderInfo = orderService.get(orderId, userId);
        var options = orderInfo.items().stream()
            .map(item -> new ProductCommand.PricingOption(
                item.productId(),
                item.productOptionId(),
                item.quantity()))
            .toList();
        var productCommand = new ProductCommand.CalculatePrice(options);
        var totalPrice = productPricingService.calculatePrice(productCommand);
        var paymentInfo = paymentService.get(orderInfo.id());
        return OrderResult.of(orderInfo, paymentInfo, totalPrice);
    }
}
