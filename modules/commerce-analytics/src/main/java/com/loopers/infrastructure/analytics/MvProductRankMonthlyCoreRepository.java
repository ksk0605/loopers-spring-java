package com.loopers.infrastructure.analytics;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.loopers.domain.analytics.MvProductRankMonthly;
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

    @Override
    public Page<MvProductRankMonthly> findAll(String periodKey, Pageable pageable) {
        return mvProductRankMonthlyJpaRepository.findAllByPeriodKey(periodKey, pageable);
    }

    @Override
    public Long size() {
        return mvProductRankMonthlyJpaRepository.count();
    }
}
