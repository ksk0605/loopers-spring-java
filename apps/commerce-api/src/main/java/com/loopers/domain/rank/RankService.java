package com.loopers.domain.rank;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RankService {
    private final RankCachedRepository rankCachedRepository;
    private final RankRepositoryRegistry repositoryRegistry;

    public List<RankedProduct> getRankRangeWithScores(RankCommand.Get command) {
        return rankCachedRepository.getRankRangeWithScores(
            command.date(),
            command.getStart(),
            command.getEnd());
    }

    public Long getTotalSize(LocalDate date) {
        return rankCachedRepository.getTotalSize(date);
    }

    public List<RankedProduct> getRankRangeWithScores(RankCommand.GetV2 command) {
        RankRepository repository = repositoryRegistry.getRepository(command.period());
        return repository.getRankRangeWithScores(command);
    }

    public Long getTotalSize(Period period) {
        return repositoryRegistry.getRepository(period).getTotalSize(period);
    }
}
