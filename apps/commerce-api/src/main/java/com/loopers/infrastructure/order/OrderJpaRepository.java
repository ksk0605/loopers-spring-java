package com.loopers.infrastructure.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.order.Order;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId);
}
