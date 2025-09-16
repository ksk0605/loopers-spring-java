package com.loopers.domain.rank;

import java.time.LocalDate;

public class RankCommand {
    public record Get(
        LocalDate date,
        int size,
        int page
    ) {
        public long getStart() {
            return (long)(page - 1) * size;
        }

        public long getEnd() {
            return getStart() + size - 1;
        }
    }
}
