package com.loopers.domain.order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Optional<Order> find(Long id);

    Order save(Order order);

    List<Order> findAll(Long userId);
}
