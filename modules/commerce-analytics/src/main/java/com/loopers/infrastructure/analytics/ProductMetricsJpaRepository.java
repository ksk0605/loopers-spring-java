package com.loopers.infrastructure.analytics;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.analytics.PeriodType;
import com.loopers.domain.analytics.ProductMetrics;

public interface ProductMetricsJpaRepository extends JpaRepository<ProductMetrics, Long> {
    Optional<ProductMetrics> findByProductIdAndPeriodTypeAndPeriodKeyAndVersion(
        Long productId,
        PeriodType periodType,
        String periodKey,
        String version
    );
}
