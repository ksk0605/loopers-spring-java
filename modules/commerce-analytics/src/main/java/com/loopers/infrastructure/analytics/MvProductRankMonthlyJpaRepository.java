package com.loopers.infrastructure.analytics;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.analytics.MvProductRankMonthly;

public interface MvProductRankMonthlyJpaRepository extends JpaRepository<MvProductRankMonthly, Long> {
    void deleteByPeriodKeyAndVersion(String periodKey, String version);

    List<MvProductRankMonthly> findByPeriodKeyAndVersionOrderByRankAsc(String periodKey, String version);
}
