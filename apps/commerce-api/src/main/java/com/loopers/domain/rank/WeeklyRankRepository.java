package com.loopers.domain.rank;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.loopers.domain.analytics.MvProductRankWeekly;
import com.loopers.domain.analytics.MvProductRankWeeklyRepository;
import com.loopers.domain.rank.RankCommand.GetV2;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WeeklyRankRepository implements RankRepository {
    private final MvProductRankWeeklyRepository mvProductRankWeeklyRepository;

    @Override
    public boolean supports(Period period) {
        return period.getPeriodType().equals(PeriodType.WEEKLY);
    }

    @Override
    public List<RankedProduct> getRankRangeWithScores(GetV2 command) {
        Page<MvProductRankWeekly> ranks = mvProductRankWeeklyRepository.findAll(
            command.period().getKey(),
            PageRequest.of(command.page() - 1, command.size()));
        return ranks.stream().map(rank -> new RankedProduct(rank.getProductId(), rank.getRank().longValue(), rank.getScore()))
            .toList();
    }

    @Override
    public Long getTotalSize(Period period) {
        return mvProductRankWeeklyRepository.size();
    }
}
