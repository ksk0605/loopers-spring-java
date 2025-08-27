package com.loopers.domain.usersignal;

import java.util.Optional;

public interface UserSignalRepository {
    UserSignal save(UserSignal userSignal);

    Optional<UserSignal> find(TargetType type, Long targetId);
}
