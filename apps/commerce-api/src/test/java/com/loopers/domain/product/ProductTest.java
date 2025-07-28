package com.loopers.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

class ProductTest {

    @DisplayName("상품을 생성할 때, ")
    @Nested
    class Create {
        @DisplayName("올바른 정보가 주어지면, 정상적으로 생성한다.")
        @Test
        void createProduct_whenValidProductInfoProvided() {
            // arrange
            String name = "테스트 상품";
            String description = "테스트 상품 설명입니다. 이 상품은 매우 멋지고 매력적입니다. 지금 바로 구매하셔야 합니다.";
            BigDecimal price = BigDecimal.valueOf(20000);
            ProductStatus status = ProductStatus.ON_SALE;
            Long brandId = 1L;
            Long categoryId = 1L;

            // act
            Product product = new Product(
                name,
                description,
                price,
                status,
                brandId,
                categoryId
            );

            // assert
            assertAll(
                () -> assertThat(product.getName()).isEqualTo(name),
                () -> assertThat(product.getDescription()).isEqualTo(description),
                () -> assertThat(product.getPrice()).isEqualTo(price),
                () -> assertThat(product.getStatus()).isEqualTo(status),
                () -> assertThat(product.getBrandId()).isEqualTo(brandId),
                () -> assertThat(product.getCategoryId()).isEqualTo(categoryId),
                () -> assertThat(product.getImages().size()).isEqualTo(0)
            );
        }

        @DisplayName("상품 이름이 null 이면, BAD REQUEST 예외를 발생한다.")
        @Test
        void throwsBadRequestException_whenNameIsNull() {
            // act
            CoreException result = assertThrows(CoreException.class, () ->
                new Product(
                    null,
                    "테스트 상품 설명입니다. 이 상품은 매우 멋지고 매력적입니다. 지금 바로 구매하셔야 합니다.",
                    BigDecimal.valueOf(20000),
                    ProductStatus.ON_SALE,
                    1L,
                    1L
                )
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("상품 이름이 빈칸으로만 이루어져있으면, BAD REQUEST 예외를 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        void throwsBadRequestException_whenNameIsBlank(String name) {
            // act
            CoreException result = assertThrows(CoreException.class, () ->
                new Product(
                    name,
                    "테스트 상품 설명입니다. 이 상품은 매우 멋지고 매력적입니다. 지금 바로 구매하셔야 합니다.",
                    BigDecimal.valueOf(20000),
                    ProductStatus.ON_SALE,
                    1L,
                    1L
                )
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("상품 설명이 빈칸으로만 이루어져있으면, BAD REQUEST 예외를 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        void throwsBadRequestException_whenDescriptionIsBlank(String description) {
            // act
            CoreException result = assertThrows(CoreException.class, () ->
                new Product(
                    "테스트 상품",
                    description,
                    BigDecimal.valueOf(20000),
                    ProductStatus.ON_SALE,
                    1L,
                    1L
                )
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("상품 설명이 최소 길이보다 작으면, BAD REQUEST 예외를 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {
            "해당 문자열은 49자 길이 입니다. 상품 설명 최소 길이를 테스트하기 위해 작성했습니다.",
            "매우 짧은 설명"
        })
        void throwsBadRequestException_whenDescriptionIsLessThanMinLength(String description) {
            // act
            CoreException result = assertThrows(CoreException.class, () ->
                new Product(
                    "테스트 상품",
                    description,
                    BigDecimal.valueOf(20000),
                    ProductStatus.ON_SALE,
                    1L,
                    1L
                )
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("상품 설명이 최대 길이보다 길면, BAD REQUEST 예외를 발생한다.")
        @Test
        void throwsBadRequestException_whenDescriptionExceedsMaxLength() {
            // act
            CoreException result = assertThrows(CoreException.class, () ->
                new Product(
                    "테스트 상품",
                    // 256자
                    "This product is designed to deliver exceptional performance and reliability. Crafted with premium materials and tested under rigorous conditions, it ensures long-lasting durability. Ideal for daily use, making it a must-have for modern living and travel!!!",
                    BigDecimal.valueOf(20000),
                    ProductStatus.ON_SALE,
                    1L,
                    1L
                )
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("상품 가격이 null 이면, BAD REQUEST 예외를 발생한다.")
        @Test
        void throwsBadRequestException_whenPriceIsNull() {
            // act
            CoreException result = assertThrows(CoreException.class, () ->
                new Product(
                    "테스트 상품",
                    "테스트 상품 설명입니다. 이 상품은 매우 멋지고 매력적입니다. 지금 바로 구매하셔야 합니다.",
                    null,
                    ProductStatus.ON_SALE,
                    1L,
                    1L
                )
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("상품 가격이 null 이면, BAD REQUEST 예외를 발생한다.")
        @Test
        void throwsBadRequestException_whenStatusIsNull() {
            // act
            CoreException result = assertThrows(CoreException.class, () ->
                new Product(
                    "테스트 상품",
                    "테스트 상품 설명입니다. 이 상품은 매우 멋지고 매력적입니다. 지금 바로 구매하셔야 합니다.",
                    BigDecimal.valueOf(20000),
                    null,
                    1L,
                    1L
                )
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("브랜드 ID 가 null 이면, BAD REQUEST 예외를 발생한다.")
        @Test
        void throwsBadRequestException_whenBrandIdIsNull() {
            // act
            CoreException result = assertThrows(CoreException.class, () ->
                new Product(
                    "테스트 상품",
                    "테스트 상품 설명입니다. 이 상품은 매우 멋지고 매력적입니다. 지금 바로 구매하셔야 합니다.",
                    BigDecimal.valueOf(20000),
                    ProductStatus.ON_SALE,
                    null,
                    1L
                )
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("카테고리 ID 가 null 이면, BAD REQUEST 예외를 발생한다.")
        @Test
        void throwsBadRequestException_whenCategoryIdIsNull() {
            // act
            CoreException result = assertThrows(CoreException.class, () ->
                new Product(
                    "테스트 상품",
                    "테스트 상품 설명입니다. 이 상품은 매우 멋지고 매력적입니다. 지금 바로 구매하셔야 합니다.",
                    BigDecimal.valueOf(20000),
                    ProductStatus.ON_SALE,
                    1L,
                    null
                )
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }

    @DisplayName("상품 상태를 변경할 때, ")
    @Nested
    class ChangeStatus {
        @DisplayName("올바른 상태가 주어지면, 해당 상태로 변경한다.")
        @Test
        void changeStatus_whenStatusIsValid() {
            // arrange
            Product product = new Product(
                "상품 이름",
                "테스트 상품 설명입니다. 이 상품은 매우 멋지고 매력적입니다. 지금 바로 구매하셔야 합니다.",
                BigDecimal.valueOf(20000),
                ProductStatus.ON_SALE,
                1L,
                1L
            );
            ProductStatus newStatus = ProductStatus.DISCONTINUED;

            // act
            product.changeStatus(newStatus);

            // assert
            assertThat(product.getStatus()).isEqualTo(newStatus);
        }

        @DisplayName("변경할 상태가 주어지지 않으면, BAD REQUEST 예외를 발생한다.")
        @Test
        void changeStatus_whenStatusIsInvalid() {
            // arrange
            Product product = new Product(
                "상품 이름",
                "테스트 상품 설명입니다. 이 상품은 매우 멋지고 매력적입니다. 지금 바로 구매하셔야 합니다.",
                BigDecimal.valueOf(20000),
                ProductStatus.ON_SALE,
                1L,
                1L
            );

            // act
            CoreException result = assertThrows(CoreException.class, () -> product.changeStatus(null));

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }

    @DisplayName("상품의 구매 가능 여부를 물을 때, ")
    @Nested
    class IsAvailable {
        @DisplayName("판매중 (ON_SALE) 상품이면, True 를 반환한다.")
        @Test
        void returnsTrue_whenProductOnSale() {
            // arrange
            Product product = new Product(
                "상품 이름",
                "테스트 상품 설명입니다. 이 상품은 매우 멋지고 매력적입니다. 지금 바로 구매하셔야 합니다.",
                BigDecimal.valueOf(20000),
                ProductStatus.ON_SALE,
                1L,
                1L
            );

            // act
            boolean result = product.isAvailable();

            // assert
            assertThat(result).isTrue();
        }

        @DisplayName("판매중(ON_SALE)이 아닌 상품 상태일 경우, False를 반환한다.")
        @ParameterizedTest
        @EnumSource(value = ProductStatus.class, names = "ON_SALE", mode = EnumSource.Mode.EXCLUDE)
        void returnsFalse_whenProductNotOnSale(ProductStatus status) {
            // arrange
            Product product = new Product(
                "상품 이름",
                "테스트 상품 설명입니다. 이 상품은 매우 멋지고 매력적입니다. 지금 바로 구매하셔야 합니다.",
                BigDecimal.valueOf(20000),
                status,
                1L,
                1L
            );

            // act
            boolean result = product.isAvailable();

            // assert
            assertThat(result).isFalse();
        }
    }

    @DisplayName("상품 이미지를 추가할 때, ")
    @Nested
    class AddImage {
        @DisplayName("정상적인 이미지 주소가 추가되면, 이미지를 추가한다.")
        @Test
        void addImage_whenValidUrlProvided() {
            // arrange
            Product product = new Product(
                "상품 이름",
                "테스트 상품 설명입니다. 이 상품은 매우 멋지고 매력적입니다. 지금 바로 구매하셔야 합니다.",
                BigDecimal.valueOf(20000),
                ProductStatus.ON_SALE,
                1L,
                1L
            );
            String url = "https://image.url";

            // act
            product.addImage(url, true);

            // assert
            assertThat(product.getImages()).hasSize(1);
            assertThat(product.getImages().get(0).getUrl()).isEqualTo(url);
            assertThat(product.getImages().get(0).isMain()).isEqualTo(true);
            assertThat(product.getImages().get(0).getSortOrder()).isEqualTo(0);
        }

        @DisplayName("이미지가 0개인 경우, 추가된 이미지는 자동으로 메인 이미지가 된다.")
        @Test
        void addImageAsMain_whenImagesIsEmpty() {
            // arrange
            Product product = new Product(
                "상품 이름",
                "테스트 상품 설명입니다. 이 상품은 매우 멋지고 매력적입니다. 지금 바로 구매하셔야 합니다.",
                BigDecimal.valueOf(20000),
                ProductStatus.ON_SALE,
                1L,
                1L
            );
            String url = "https://image.url";

            // act
            product.addImage(url, false);

            // assert
            assertThat(product.getImages()).hasSize(1);
            assertThat(product.getImages().get(0).getUrl()).isEqualTo(url);
            assertThat(product.getImages().get(0).isMain()).isEqualTo(true);
            assertThat(product.getImages().get(0).getSortOrder()).isEqualTo(0);
        }

        @DisplayName("이미지가 1개 이상 존재하면, 추가된 이미지의 순서는 맨 마지막 순서이다.")
        @Test
        void addImageAsLast_whenImagesIsNotEmpty() {
            // arrange
            Product product = new Product(
                "상품 이름",
                "테스트 상품 설명입니다. 이 상품은 매우 멋지고 매력적입니다. 지금 바로 구매하셔야 합니다.",
                BigDecimal.valueOf(20000),
                ProductStatus.ON_SALE,
                1L,
                1L
            );
            product.addImage("https://image.url", false);

            // act
            String newImageUrl = "https://image2.url";
            product.addImage(newImageUrl, false);

            // assert

            assertAll(() -> assertThat(product.getImages()).hasSize(2),
                () -> assertThat(product.getImages().get(0).getSortOrder()).isEqualTo(0),
                () -> assertThat(product.getImages().get(0).isMain()).isTrue(),
                () -> assertThat(product.getImages().get(1).getSortOrder()).isEqualTo(1),
                () -> assertThat(product.getImages().get(1).getImageUrl()).isEqualTo(newImageUrl),
                () -> assertThat(product.getImages().get(1).isMain()).isFalse()
            );
        }
    }
}
