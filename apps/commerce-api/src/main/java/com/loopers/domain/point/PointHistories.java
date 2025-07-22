package com.loopers.domain.point;

import java.util.List;

public class PointHistories {
    private final List<PointHistory> histories;

    public PointHistories(List<PointHistory> pointHistories) {
        this.histories = pointHistories;
    }

    public int getBalance() {
        return histories.stream()
            .mapToInt(PointHistory::getAmount)
            .sum();
    }
}
