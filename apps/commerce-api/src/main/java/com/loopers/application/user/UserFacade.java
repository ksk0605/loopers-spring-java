package com.loopers.application.user;

import com.loopers.domain.user.User;
import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserService;
import com.loopers.support.annotation.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;

    public UserResult getUser(String userId) {
        User user = userService.get(userId);
        return UserResult.from(user);
    }

    public UserResult createUser(UserCommand.Create command) {
        User user = userService.createUser(command);
        return UserResult.from(user);
    }
}
