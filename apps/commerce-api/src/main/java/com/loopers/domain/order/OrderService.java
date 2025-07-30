package com.loopers.domain.order;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    @Transactional
    public Order place(Long userId, List<OrderItem> items) {
        Order order = new Order(userId, items);
        order.place(orderValidator);
        return orderRepository.save(order);
    }
}
