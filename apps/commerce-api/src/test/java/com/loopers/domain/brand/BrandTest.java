package com.loopers.domain.brand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

class BrandTest {

    @DisplayName("브랜드를 생성할 때, ")
    @Nested
    class Create {
        @DisplayName("필요한 정보가 모두 주어지면, 정상적으로 생성된다.")
        @ParameterizedTest
        @ValueSource(strings = {
            "10자 브랜드 설명",
            "이 문자열은 오십 글자입니다. 설명의 최대 길이 초과 여부를 테스트 하기 위한 예시입니다."
        })
        void createBrand_whenValidBrandInfoProvided(String description) {
            // arrange
            String name = "테스트 브랜드";
            String logoUrl = "https://test.logo.url";

            // act
            Brand brand = new Brand(name, description, logoUrl);

            // assert
            assertAll(
                () -> assertThat(brand.getName()).isEqualTo(name),
                () -> assertThat(brand.getDescription()).isEqualTo(description),
                () -> assertThat(brand.getLogoUrl()).isEqualTo(logoUrl)
            );
        }

        @DisplayName("브랜드 이름이 빈칸으로만 이루어져 있으면, BAD_REQUEST 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        void throwsBadRequestException_whenNameIsBlank(String name) {
            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                new Brand(name, "테스트 브랜드 설명", "https://test.logo.url");
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("브랜드 설명이 빈칸으로만 이루어져 있으면, BAD_REQUEST 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        void throwsBadRequestException_whenDescriptionIsBlank(String description) {
            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                new Brand("브랜드 명", description, "https://test.logo.url");
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("브랜드 이름이 빈칸으로만 이루어져 있으면, BAD_REQUEST 예외가 발생한다.")
        @Test
        void throwsBadRequestException_whenNameIsNull() {
            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                new Brand(null, "테스트 브랜드 설명", "https://test.logo.url");
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("브랜드 설명이 10자 미만이면, BAD_REQUEST 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"2자", "아홉자 브랜드설명"})
        void throwsBadRequestException_whenDescriptionIsLessThanMinLength(String description) {
            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                new Brand("테스트 브랜드", description, "https://test.logo.url");
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("브랜드 설명이 50자 초과이면, BAD_REQUEST 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {
            "이 문자열은 오십일 글자입니다. 설명의 최대 길이 초과 여부를 테스트 하기 위한 예시입니다.",
            "이 문자열은 오십 글자를 한참 넘는 문장입니다. 설명의 최대 길이 초과 여부를 테스트 하기 위한 예시입니다."
        })
        void throwsBadRequestException_whenDescriptionExceedsMaxLength(String description) {
            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                new Brand("테스트 브랜드", description, "https://test.logo.url");
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }
}
