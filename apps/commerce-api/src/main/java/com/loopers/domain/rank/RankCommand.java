package com.loopers.domain.rank;

public class RankCommand {
    public record GetV2(
        Period period,
        int size,
        int page) {
        public static GetV2 of(String periodKey, String periodType, int size, int page) {
            if (size < 1) {
                throw new IllegalArgumentException("size는 1 이상이어야 합니다.");
            }
            if (page < 1) {
                throw new IllegalArgumentException("page는 1 이상이어야 합니다.");
            }
            Period period = Period.of(periodKey, periodType);
            return new GetV2(period, size, page);
        }

        public Long getStart() {
            return (long)(page - 1) * size;
        }

        public Long getEnd() {
            return getStart() + size - 1;
        }
    }
}
