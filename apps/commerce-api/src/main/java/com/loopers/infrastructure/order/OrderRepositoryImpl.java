package com.loopers.infrastructure.order;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository jpaRepository;

    @Override
    public Optional<Order> find(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Order save(Order order) {
        return jpaRepository.save(order);
    }
}
