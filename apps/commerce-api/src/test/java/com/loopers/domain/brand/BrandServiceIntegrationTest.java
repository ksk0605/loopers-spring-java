package com.loopers.domain.brand;

import static com.loopers.support.fixture.BrandFixture.aBrand;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.support.IntegrationTest;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

@SpringBootTest
class BrandServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private BrandService brandService;

    @Autowired
    @MockitoSpyBean
    private BrandJpaRepository brandJpaRepository;

    @DisplayName("브랜드를 조회할 때, ")
    @Nested
    class Get {
        @DisplayName("존재하는 예시 ID를 주면, 해당 예시 정보를 반환한다.")
        @Test
        void returnsBrandInfo_whenValidIdIsProvided() {
            // arrange
            Brand brand = brandJpaRepository.save(aBrand().build());

            // act
            Brand result = brandService.get(brand.getId());

            // assert
            assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getId()).isEqualTo(brand.getId()),
                () -> assertThat(result.getName()).isEqualTo(brand.getName()),
                () -> assertThat(result.getDescription()).isEqualTo(brand.getDescription())
            );
        }

        @DisplayName("존재하지 않는 예시 ID를 주면, NOT_FOUND 예외가 발생한다.")
        @Test
        void throwsException_whenInvalidIdIsProvided() {
            // arrange
            Long invalidId = 999L; // Assuming this ID does not exist

            // act
            CoreException exception = assertThrows(CoreException.class, () -> {
                brandService.get(invalidId);
            });

            // assert
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }
    }
}
