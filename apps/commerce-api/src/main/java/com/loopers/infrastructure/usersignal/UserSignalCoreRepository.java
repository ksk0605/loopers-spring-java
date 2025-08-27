package com.loopers.infrastructure.usersignal;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.loopers.domain.usersignal.TargetType;
import com.loopers.domain.usersignal.UserSignal;
import com.loopers.domain.usersignal.UserSignalRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSignalCoreRepository implements UserSignalRepository {
    private final UserSignalJpaRepository userSignalJpaRepository;

    @Override
    public UserSignal save(UserSignal userSignal) {
        return userSignalJpaRepository.save(userSignal);
    }

    @Override
    public Optional<UserSignal> find(TargetType type, Long targetId) {
        return userSignalJpaRepository.findByTargetForUpdate(type, targetId);
    }
}
