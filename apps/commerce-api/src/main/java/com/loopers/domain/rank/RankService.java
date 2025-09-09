package com.loopers.domain.rank;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RankService {
    private final RankRepository rankRepository;

    public List<RankedProduct> getRankRangeWithScores(RankCommand.Get command) {
        return rankRepository.getRankRangeWithScores(
            command.date(),
            command.getStart(),
            command.getEnd());
    }
}
