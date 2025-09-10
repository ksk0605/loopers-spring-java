package com.loopers.domain.rank;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.loopers.support.IntegrationTest;

class RankServiceIntegrationTest extends IntegrationTest {
    @Autowired
    private RankService rankService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @DisplayName("날짜와 사이즈, 페이지가 주어지면 범위에 해당하는 상품 ID 별 스코어를 정렬된 상태로 반환한다.")
    @Test
    void returnsRankedProduct() {
        // arrange
        LocalDate today = LocalDate.now();
        String rankingKey = "rank:all:" + today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 총 30개의 상품을 랭킹에 추가 (점수: 30.0 ~ 1.0)
        IntStream.rangeClosed(1, 30).forEach(i -> {
            redisTemplate.opsForZSet().add(rankingKey, String.valueOf(31 - i), (double)(31 - i));
        });

        RankCommand.Get command = new RankCommand.Get(today, 10, 1);

        // act
        List<RankedProduct> rankedProducts = rankService.getRankRangeWithScores(command);

        // assert
        assertThat(rankedProducts).hasSize(10);
        assertThat(rankedProducts.get(0).rank()).isEqualTo(1);
        assertThat(rankedProducts.get(0).productId()).isEqualTo(30L);
    }
}
