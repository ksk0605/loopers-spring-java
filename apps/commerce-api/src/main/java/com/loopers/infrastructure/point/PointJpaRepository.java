package com.loopers.infrastructure.point;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.point.PointHistory;

public interface PointJpaRepository extends JpaRepository<PointHistory, Long> {
}
