package com.loopers.domain.rank;

import java.util.List;

import org.springframework.stereotype.Component;

import com.loopers.domain.rank.RankCommand.GetV2;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DailyRankRepository implements RankRepository {
    private final RankCachedRepository rankCachedRepository;

    @Override
    public boolean supports(Period period) {
        return period.getPeriodType().equals(PeriodType.DAILY);
    }

    @Override
    public List<RankedProduct> getRankRangeWithScores(GetV2 command) {
        return rankCachedRepository.getRankRangeWithScores(command.period().getDate(), command.getStart(), command.getEnd());
    }

    @Override
    public Long getTotalSize(Period period) {
        return rankCachedRepository.getTotalSize(period.getDate());
    }
}
