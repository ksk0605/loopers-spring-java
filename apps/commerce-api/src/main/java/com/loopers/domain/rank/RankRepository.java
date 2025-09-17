package com.loopers.domain.rank;

import java.util.List;

public interface RankRepository {
    boolean supports(Period period);

    List<RankedProduct> getRankRangeWithScores(RankCommand.GetV2 command);

    Long getTotalSize(Period period);
}
