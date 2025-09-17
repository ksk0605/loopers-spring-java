package com.loopers.domain.analytics;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MvProductRankMonthlyRepository {
    void delete(String periodKey, String Version);

    Page<MvProductRankMonthly> findAll(String periodKey, Pageable pageable);

    Long size();
}
