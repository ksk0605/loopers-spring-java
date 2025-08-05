package com.loopers.domain.user;

import static com.loopers.support.util.RequireUtils.requireNonEmpty;
import static com.loopers.support.util.RequireUtils.requireNotNull;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    private static final String USER_ID_PATTERN = "^[a-zA-Z0-9]+$";
    private static final int MAX_USER_ID_LENGTH = 10;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String BIRTH_DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";
    private static final int INITIAL_POINT = 0;

    private String userId;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    private String email;

    private int point;

    @Version
    private Long version;

    public User(String userId, Gender gender, String birthDate, String email) {
        validateUserId(userId);
        validateBirthDate(birthDate);
        validateEmail(email);

        this.userId = requireNonEmpty(userId, "ID는 비어있을 수 없습니다.");
        this.gender = requireNotNull(gender, "성별은 필수 정보 입니다.");
        this.birthDate = LocalDate.parse(birthDate);
        this.email = requireNonEmpty(email, "이메일은 비어있을 수 없습니다.");
        this.point = INITIAL_POINT;
    }

    private void validateUserId(String userId) {
        if (!userId.matches(USER_ID_PATTERN)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "ID는 영문자와 숫자만 허용됩니다.");
        }
        if (userId.length() > MAX_USER_ID_LENGTH) {
            throw new CoreException(ErrorType.BAD_REQUEST, "ID는 10자 이내여야 합니다.");
        }
    }

    private void validateBirthDate(String birthDate) {
        requireNonEmpty(birthDate, "생년월일은 비어있을 수 없습니다.");

        if (!birthDate.matches(BIRTH_DATE_PATTERN)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 yyyy-MM-dd 형식이어야 합니다.");
        }
        try {
            LocalDate.parse(birthDate);
        } catch (DateTimeParseException e) {
            throw new CoreException(ErrorType.BAD_REQUEST, "유효하지 않은 날짜입니다.");
        }
    }

    private void validateEmail(String email) {
        if (!email.matches(EMAIL_PATTERN)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "이메일 형식이 올바르지 않습니다.");
        }
    }

    public void updatePoint(int balance) {
        this.point = balance;
    }

    public void usePoint(int amount) {
        this.point -= amount;
    }

    public void chargePoint(int amount) {
        this.point += amount;
    }
}
