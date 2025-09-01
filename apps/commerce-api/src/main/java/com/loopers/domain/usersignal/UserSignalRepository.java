package com.loopers.domain.usersignal;

import java.util.List;
import java.util.Optional;

public interface UserSignalRepository {
    UserSignal save(UserSignal userSignal);

    Optional<UserSignal> find(TargetType type, Long targetId);

    List<UserSignal> findAllIn(List<Long> productIds, TargetType targetType);
}
