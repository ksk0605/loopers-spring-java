package com.loopers.domain.brand;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.Getter;

@Getter
public class Brand {
    private static final int MINIMUM_DESCRIPTION_SIZE = 10;
    private static final int MAXIMUM_DESCRIPTION_SIZE = 50;

    private Long id;
    private String name;
    private String description;
    private String logoUrl;

    public Brand(String name, String description, String logoUrl) {
        validateName(name);
        validateDescription(description);
        this.name = name;
        this.description = description;
        this.logoUrl = logoUrl;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "브랜드 이름은 비어있을 수 없습니다.");
        }
    }

    private void validateDescription(String description) {
        if (description == null) {
            return;
        }
        if (description.isBlank()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "브랜드 설명은 빈칸으로 작성할 수 없습니다.");
        }
        if (description.length() < MINIMUM_DESCRIPTION_SIZE) {
            throw new CoreException(ErrorType.BAD_REQUEST, "브랜드 설명은 10자 이상이어야 합니다.");
        }
        if (description.length() > MAXIMUM_DESCRIPTION_SIZE) {
            throw new CoreException(ErrorType.BAD_REQUEST, "브랜드 설명은 50자 미만이어야 합니다.");
        }
    }
}
