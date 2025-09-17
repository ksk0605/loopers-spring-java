package com.loopers.domain.analytics;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MvProductRankWeeklyRepository {
    void delete(String periodKey, String version);

    Page<MvProductRankWeekly> findAll(String periodKey, Pageable pageable);

    Long size();
}
