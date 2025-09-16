package com.loopers.infrastructure.analytics;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.analytics.MvProductRankWeekly;

public interface MvProductRankWeeklyJpaRepository extends JpaRepository<MvProductRankWeekly, Long> {
    void deleteByPeriodKeyAndVersion(String periodKey, String version);

    List<MvProductRankWeekly> findByPeriodKeyAndVersionOrderByRankAsc(String periodKey, String version);
}
