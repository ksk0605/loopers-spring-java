package com.loopers.domain.order;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    @Transactional
    public Order create(OrderCommand.Order command) {
        Order order = Order.from(command);
        order.place(orderValidator);
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> getAll(Long userId) {
        return orderRepository.findAll(userId);
    }

    @Transactional(readOnly = true)
    public Order get(Long orderId, Long userId) {
        return orderRepository.find(orderId, userId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 주문입니다."));
    }

    @Transactional
    public void completePayment(String orderId) {
        Order order = orderRepository.find(orderId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "주문이 존재하지 않습니다. [orderId = " + orderId + "]"));
        order.completePayment();
    }
}
