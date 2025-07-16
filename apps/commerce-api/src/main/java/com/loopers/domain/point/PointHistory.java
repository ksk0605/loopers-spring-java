package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "point_history")
public class PointHistory extends BaseEntity {

    private static final int MIN_POINT = 0;

    private String userId;

    private int amount;

    private int balance;

    @Enumerated(EnumType.STRING)
    private PointHistoryType type;

    protected PointHistory() {
    }

    public PointHistory(String userId, int amount, int balance, PointHistoryType type) {
        if (amount <= MIN_POINT) {
            throw new CoreException(ErrorType.BAD_REQUEST, "포인트는 0 이하의 정수로 포인트를 충전할 수 없습니다.");
        }
        this.userId = userId;
        this.amount = amount;
        this.balance = balance;
        this.type = type;
    }

    public static PointHistory earn(String userId, int currentBalance, int amount) {
        return new PointHistory(
            userId,
            amount,
            currentBalance + amount,
            PointHistoryType.EARN
        );
    }

    public String getUserId() {
        return userId;
    }

    public int getAmount() {
        return amount;
    }

    public int getBalance() {
        return balance;
    }

    public PointHistoryType getType() {
        return type;
    }
}
