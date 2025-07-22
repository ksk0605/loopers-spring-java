package com.loopers.infrastructure.point;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.point.PointHistory;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistory, Long> {
    List<PointHistory> findAllByUserId(String userId);
}
