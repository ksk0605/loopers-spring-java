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
    public OrderInfo place(OrderCommand.Place command) {
        List<OrderItem> orderItems = command.options().stream()
            .map(OrderCommand.OrderOption::toOrderItem)
            .toList();
        Order order = new Order(command.userId(), orderItems);
        order.place(orderValidator);
        return OrderInfo.from(orderRepository.save(order));
    }

    @Transactional
    public OrderInfo pay(Long orderId) {
        Order order = orderRepository.find(orderId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 주문입니다."));
        order.pay();
        return OrderInfo.from(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderInfo> getAll(Long userId) {
        return orderRepository.findAll(userId).stream()
            .map(OrderInfo::from)
            .toList();
    }
}
