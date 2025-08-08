package com.loopers.interfaces.api.user;

import java.time.LocalDate;

import com.loopers.application.user.UserResult;

import jakarta.validation.constraints.NotNull;

public class UserV1Dto {
    public record CreateUserRequest(
        String userId,
        @NotNull Gender gender,
        String birthDate,
        String email
    ) {
    }

    public record UserResponse(
        Long id,
        String userId,
        Gender gender,
        LocalDate birthDate,
        String email
    ) {
        public static UserResponse from(UserResult userResult) {
            return new UserResponse(
                userResult.id(),
                userResult.userId(),
                Gender.from(userResult.gender().name()),
                userResult.birthDate(),
                userResult.email()
            );
        }
    }

    public enum Gender {
        MALE,
        FEMALE;

        public static Gender from(String gender) {
            return Gender.valueOf(gender.toUpperCase());
        }
    }
}
