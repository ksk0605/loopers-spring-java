package com.loopers.domain.user;

import java.time.LocalDate;

public record UserInfo(
    Long id,
    String userId,
    Gender gender,
    LocalDate birthDate,
    String email,
    int point
) {
    public static UserInfo from(User user) {
        return new UserInfo(
            user.getId(),
            user.getUserId(),
            user.getGender(),
            user.getBirthDate(),
            user.getEmail(),
            user.getPoint()
        );
    }
}
