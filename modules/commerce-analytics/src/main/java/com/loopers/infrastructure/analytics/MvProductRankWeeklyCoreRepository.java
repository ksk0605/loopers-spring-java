package com.loopers.infrastructure.analytics;

import org.springframework.stereotype.Component;

import com.loopers.domain.analytics.MvProductRankWeeklyRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MvProductRankWeeklyCoreRepository implements MvProductRankWeeklyRepository {
    private final MvProductRankMonthlyJpaRepository mvProductRankMonthlyJpaRepository;

    @Override
    public void delete(String periodKey, String version) {
        mvProductRankMonthlyJpaRepository.deleteByPeriodKeyAndVersion(periodKey, version);
    }
}
