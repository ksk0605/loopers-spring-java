package com.loopers.domain.rank;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.loopers.domain.analytics.MvProductRankMonthly;
import com.loopers.domain.analytics.MvProductRankMonthlyRepository;
import com.loopers.domain.rank.RankCommand.GetV2;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MonthlyRankRepository implements RankRepository {
    private final MvProductRankMonthlyRepository mvProductRankMonthlyRepository;

    @Override
    public boolean supports(Period period) {
        return period.getPeriodType().equals(PeriodType.MONTHLY);
    }

    @Override
    public List<RankedProduct> getRankRangeWithScores(GetV2 command) {
        Page<MvProductRankMonthly> ranks = mvProductRankMonthlyRepository
            .findAll(command.period().getKey(), PageRequest.of(command.page() - 1, command.size()));
        return ranks.stream()
            .map(rank -> new RankedProduct(rank.getProductId(), rank.getRank().longValue(), rank.getScore()))
            .toList();
    }

    @Override
    public Long getTotalSize(Period period) {
        return mvProductRankMonthlyRepository.size();
    }
}
