package com.loopers.domain.rank;

import java.time.LocalDate;
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

    public Long getTotalSize(LocalDate date) {
        return rankRepository.getTotalSize(date);
    }
}
