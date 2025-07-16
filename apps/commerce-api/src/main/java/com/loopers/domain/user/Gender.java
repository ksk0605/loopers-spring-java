package com.loopers.domain.user;

public enum Gender {
    MALE,
    FEMALE;

    public static Gender getGender(String gender) {
        return Gender.valueOf(gender.toUpperCase());
    }
}
