package com.loopers.domain.order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Optional<Order> find(Long id);

    Optional<Order> find(Long id, Long userId);

    Optional<Order> find(String orderId);

    Order save(Order order);

    List<Order> findAll(Long userId);
}
