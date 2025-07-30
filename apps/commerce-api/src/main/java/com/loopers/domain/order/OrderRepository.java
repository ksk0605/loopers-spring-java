package com.loopers.domain.order;

import java.util.Optional;

public interface OrderRepository {
    Optional<Order> find(Long id);
}
