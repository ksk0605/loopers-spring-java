package com.loopers.domain.analytics;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProductMetricsRepository {

    Optional<ProductMetrics> find(Long productId, PeriodType periodType, String periodKey, String version);

    ProductMetrics save(ProductMetrics metrics);

    ProductMetrics upsert(
        Long productId,
        PeriodType periodType,
        String periodKey,
        String version,
        Long likes,
        Long views,
        Long orderCount,
        LocalDateTime generatedAt
    );
}
