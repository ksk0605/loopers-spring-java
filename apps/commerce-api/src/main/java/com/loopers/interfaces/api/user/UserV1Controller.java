package com.loopers.interfaces.api.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserResult;
import com.loopers.domain.user.UserCommand;
import com.loopers.interfaces.api.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserV1Controller implements UserV1ApiSpec {
    private final UserFacade userFacade;

    @PostMapping
    @Override
    public ApiResponse<UserV1Dto.UserResponse> createUser(
        @RequestBody @Valid UserV1Dto.CreateUserRequest request
    ) {
        var command = new UserCommand.Create(
            request.userId(),
            request.gender().name(),
            request.birthDate(),
            request.email()
        );
        UserResult result = userFacade.createUser(command);
        UserV1Dto.UserResponse response = UserV1Dto.UserResponse.from(result);
        return ApiResponse.success(response);
    }

    @GetMapping("/me")
    public ApiResponse<UserV1Dto.UserResponse> getMe(
        @RequestHeader("X-USER-ID") String userId
    ) {
        UserResult result = userFacade.getUser(userId);
        return ApiResponse.success(UserV1Dto.UserResponse.from(result));
    }
}
