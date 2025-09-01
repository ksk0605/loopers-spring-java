package com.loopers.support.fixture;

import com.loopers.domain.usersignal.TargetType;
import com.loopers.domain.usersignal.UserSignal;

public class UserSignalFixture {
    private TargetType targetType = TargetType.PRODUCT;
    private Long targetId = 1L;

    public static UserSignalFixture anUserSignal() {
        return new UserSignalFixture();
    }

    public UserSignalFixture targetType(TargetType targetType) {
        this.targetType = targetType;
        return this;
    }

    public UserSignalFixture targetId(Long targetId) {
        this.targetId = targetId;
        return this;
    }

    public UserSignal build() {
        return new UserSignal(
            targetId,
            targetType
        );
    }
}
