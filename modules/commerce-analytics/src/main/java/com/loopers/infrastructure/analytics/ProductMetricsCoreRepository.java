package com.loopers.infrastructure.analytics;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.analytics.PeriodType;
import com.loopers.domain.analytics.ProductMetrics;
import com.loopers.domain.analytics.ProductMetricsRepository;

@Component
@Transactional
public class ProductMetricsCoreRepository implements ProductMetricsRepository {

    private final ProductMetricsJpaRepository jpaRepository;

    public ProductMetricsCoreRepository(ProductMetricsJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<ProductMetrics> find(Long productId, PeriodType periodType, String periodKey, String version) {
        return jpaRepository.findByProductIdAndPeriodTypeAndPeriodKeyAndVersion(productId, periodType, periodKey, version);
    }

    @Override
    public ProductMetrics save(ProductMetrics metrics) {
        return jpaRepository.save(metrics);
    }

    @Override
    public ProductMetrics upsert(Long productId,
        PeriodType periodType,
        String periodKey,
        String version,
        Long likes,
        Long views,
        Long orderCount,
        LocalDateTime generatedAt) {
        try {
            var existing = find(productId, periodType, periodKey, version);
            if (existing.isPresent()) {
                var entity = existing.get();
                entity.updateValues(likes, views, orderCount, generatedAt);
                return save(entity);
            }
            var created = new ProductMetrics(productId, periodType, periodKey, version, likes, views, orderCount, generatedAt);
            return save(created);
        } catch (DataIntegrityViolationException e) {
            // handle rare race: another thread inserted same natural key
            var retry = find(productId, periodType, periodKey, version)
                .orElseThrow(() -> e);
            retry.updateValues(likes, views, orderCount, generatedAt);
            return save(retry);
        }
    }
}
