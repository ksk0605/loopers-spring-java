package com.loopers.domain.point;

import org.springframework.stereotype.Component;

@Component
public class PointHistoryService {
    private final PointHistoryRepository pointHistoryRepository;

    public PointHistoryService(PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }

    public PointHistory earn(String userId, int amount) {
        PointHistories histories = new PointHistories(pointHistoryRepository.findAll(userId));
        int currentBalance = histories.getBalance();
        return pointHistoryRepository.save(
            PointHistory.earn(userId, currentBalance, amount)
        );
    }
}
