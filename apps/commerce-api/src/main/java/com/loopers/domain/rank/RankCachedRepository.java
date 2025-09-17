package com.loopers.domain.rank;

import java.time.LocalDate;
import java.util.List;

public interface RankCachedRepository {
    
    List<RankedProduct> getRankRangeWithScores(LocalDate date, Long start, Long end);

    Long getTotalSize(LocalDate date);
}
