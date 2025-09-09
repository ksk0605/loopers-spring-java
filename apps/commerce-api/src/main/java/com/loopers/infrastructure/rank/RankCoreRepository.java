package com.loopers.infrastructure.rank;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import com.loopers.domain.rank.RankRepository;
import com.loopers.domain.rank.RankedProduct;
import com.loopers.infrastructure.redis.RedisCacheRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RankCoreRepository implements RankRepository {
    private static final String KEY_PREFIX = "rank:all:";
    private final RedisCacheRepository redisCacheRepository;

    @Override
    public List<RankedProduct> getRankRangeWithScores(LocalDate date, Long start, Long end) {
        String key = generateKey(date);
        Set<ZSetOperations.TypedTuple<Object>> rankedTuples = redisCacheRepository.getRankRangeWithScores(key, start, end);

        if (rankedTuples == null || rankedTuples.isEmpty()) {
            return List.of();
        }

        List<ZSetOperations.TypedTuple<Object>> tupleList = new ArrayList<>(rankedTuples);
        return IntStream.range(0, tupleList.size())
            .mapToObj(index -> {
                ZSetOperations.TypedTuple<Object> tuple = tupleList.get(index);

                if (tuple.getValue() instanceof String) {
                    try {
                        long productId = Long.parseLong((String)tuple.getValue());
                        double score = (tuple.getScore() != null) ? tuple.getScore() : 0.0;
                        long currentRank = start + index + 1;
                        return new RankedProduct(productId, currentRank, score);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
                return null;
            })
            .filter(Objects::nonNull)
            .toList();
    }

    private String generateKey(LocalDate date) {
        return KEY_PREFIX + date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
