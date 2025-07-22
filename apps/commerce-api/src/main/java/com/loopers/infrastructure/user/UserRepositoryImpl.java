package com.loopers.infrastructure.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.loopers.domain.user.User;
import com.loopers.domain.user.UserRepository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<User> find(String userId) {
        return userJpaRepository.findByUserId(userId);
    }
}
