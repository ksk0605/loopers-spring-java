package com.loopers.domain.brand;

import static com.loopers.support.util.RequireUtils.require;
import static com.loopers.support.util.RequireUtils.requireNonEmpty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "brand")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand {
    private static final int MINIMUM_DESCRIPTION_SIZE = 10;
    private static final int MAXIMUM_DESCRIPTION_SIZE = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    private String logoUrl;

    public Brand(String name, String description, String logoUrl) {
        this.name = requireNonEmpty(name, "브랜드 이름은 비어있을 수 없습니다.");
        this.description = description;
        this.logoUrl = logoUrl;

        if (description != null) 
            validateDescription(description);
    }

    private void validateDescription(String description) {
        require(!description.isBlank(), "브랜드 설명은 빈칸으로 작성할 수 없습니다.");
        require(description.length() >= MINIMUM_DESCRIPTION_SIZE, "브랜드 설명은 10자 이상이어야 합니다.");
        require(description.length() <= MAXIMUM_DESCRIPTION_SIZE, "브랜드 설명은 50자 미만이어야 합니다.");
    }
}
