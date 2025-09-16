package com.loopers.infrastructure.analytics;

import org.springframework.stereotype.Component;

import com.loopers.domain.analytics.MvProductRankMonthlyRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MvProductRankMonthlyCoreRepository implements MvProductRankMonthlyRepository {
    private final MvProductRankMonthlyJpaRepository mvProductRankMonthlyJpaRepository;

    @Override
    public void delete(String periodKey, String Version) {
        mvProductRankMonthlyJpaRepository.deleteByPeriodKeyAndVersion(periodKey, Version);
    }
}
