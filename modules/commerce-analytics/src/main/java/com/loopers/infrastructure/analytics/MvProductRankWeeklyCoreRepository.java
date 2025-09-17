package com.loopers.infrastructure.analytics;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.loopers.domain.analytics.MvProductRankWeekly;
import com.loopers.domain.analytics.MvProductRankWeeklyRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MvProductRankWeeklyCoreRepository implements MvProductRankWeeklyRepository {
    private final MvProductRankWeeklyJpaRepository mvProductRankWeeklyJpaRepository;

    @Override
    public void delete(String periodKey, String version) {
        mvProductRankWeeklyJpaRepository.deleteByPeriodKeyAndVersion(periodKey, version);
    }

    @Override
    public Page<MvProductRankWeekly> findAll(String periodKey, Pageable pageable) {
        return mvProductRankWeeklyJpaRepository.findAllByPeriodKey(periodKey, pageable);
    }

    @Override
    public Long size() {
        return mvProductRankWeeklyJpaRepository.count();
    }
}
