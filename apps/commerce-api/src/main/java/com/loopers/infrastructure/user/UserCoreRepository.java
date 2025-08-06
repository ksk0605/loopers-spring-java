package com.loopers.infrastructure.user;

import java.util.Optional;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

import com.loopers.domain.user.User;
import com.loopers.domain.user.UserRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

@Repository
public class UserCoreRepository implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserCoreRepository(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<User> find(String userId) {
        try {
            return userJpaRepository.findByUserId(userId);
        } catch (OptimisticLockingFailureException exception) {
            throw new CoreException(ErrorType.CONFLICT);
        }
    }

    @Override
    public Optional<User> find(Long id) {
        try {
            return userJpaRepository.findById(id);
        } catch (OptimisticLockingFailureException exception) {
            throw new CoreException(ErrorType.CONFLICT);
        }
    }

}
