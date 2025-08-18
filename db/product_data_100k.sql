-- 100개의 브랜드 데이터 삽입

SET SESSION cte_max_recursion_depth = 100000000;

INSERT INTO brand (name, description, logo_url)
WITH RECURSIVE brand_data AS (
    SELECT
        1 AS id,
        CONCAT(CAST('Brand ' AS CHAR(20)), 1) AS name,
        CONCAT(CAST('This is a dummy description for product ' AS CHAR(42)), 1, CAST('. It is a long description to meet the minimum length requirement.' AS CHAR (80))) AS description
    UNION ALL
    SELECT
        id + 1,
        CONCAT('Brand ', id + 1),
        CONCAT(CAST('This is a dummy description for product ' AS CHAR(42)), id + 1, CAST('. It is a long description to meet the minimum length requirement.' AS CHAR (80))) AS description
    FROM brand_data
    WHERE id < 100
)
SELECT
    name,
    description,
    CONCAT('http://example.com/logo', id, '.png')
FROM brand_data;

-- 100,000개의 상품 데이터 삽입
-- category_id에 1부터 5 사이의 랜덤 값을 부여합니다.
INSERT INTO product (
    created_at,
    updated_at,
    name,
    description,
    price,
    status,
    category_id,
    brand_id,
    sale_start_date
)
WITH RECURSIVE product_data AS (
    SELECT
        1 AS id,
        CAST(RAND() * 99 + 1 AS UNSIGNED) AS brand_id_tmp,
        CAST(RAND() * 4 + 1 AS UNSIGNED) AS category_id_tmp
    UNION ALL
    SELECT
        id + 1,
        CAST(RAND() * 99 + 1 AS UNSIGNED),
        CAST(RAND() * 4 + 1 AS UNSIGNED)
    FROM product_data
    WHERE id < 100000
    LIMIT 100000
    )
SELECT
    NOW(),
    NOW(),
    CONCAT(CAST('Product Name ' AS CHAR(30)), id),
    CONCAT(CAST('This is a dummy description for product ' AS CHAR(40)), id, CAST('. It is a long description to meet the minimum length requirement.' AS CHAR(120))),
    CAST(RAND() * 100000 + 1000 AS DECIMAL(10, 2)),
    'ON_SALE',
    category_id_tmp,
    brand_id_tmp,
    DATE_ADD(NOW(), INTERVAL CAST(RAND() * 365 AS UNSIGNED) DAY)
FROM product_data;

-- 100,000개 상품에 대한 이미지 데이터 삽입 (상품당 1~5개 이미지)

INSERT INTO product_image (created_at, updated_at, product_id, image_url, is_main, sort_order)
WITH RECURSIVE image_data AS (
    SELECT 1 AS product_id_tmp, 1 AS image_idx
    UNION ALL
    SELECT
        CASE WHEN image_idx < CAST(RAND() * 4 + 1 AS UNSIGNED) THEN product_id_tmp ELSE product_id_tmp + 1 END,
        CASE WHEN image_idx < CAST(RAND() * 4 + 1 AS UNSIGNED) THEN image_idx + 1 ELSE 1 END
    FROM image_data
    WHERE product_id_tmp < 100000
)
SELECT
    NOW(),
    NOW(),
    product_id_tmp,
    CONCAT(CAST('http://example.com/product/' AS CHAR(50)), product_id_tmp, CAST('/image' AS CHAR(8)), image_idx, CAST('.jpg' AS CHAR(8))),
    CASE WHEN image_idx = 1 THEN TRUE ELSE FALSE END,
    image_idx - 1
FROM image_data;


-- 100,000개 상품에 대한 옵션 데이터 삽입 (상품당 1~3개 옵션)
INSERT INTO product_option (created_at, updated_at, product_id, option_type, option_value, additional_price)
WITH RECURSIVE option_data AS (
    SELECT 1 AS product_id_tmp, 1 AS option_idx
    UNION ALL
    SELECT
        CASE WHEN option_idx < CAST(RAND() * 2 + 1 AS UNSIGNED) THEN product_id_tmp ELSE product_id_tmp + 1 END,
        CASE WHEN option_idx < CAST(RAND() * 2 + 1 AS UNSIGNED) THEN option_idx + 1 ELSE 1 END
    FROM option_data
    WHERE product_id_tmp < 100000
)
SELECT
    NOW(),
    NOW(),
    product_id_tmp,
    CONCAT('Color'), -- 더미 옵션 타입
    CONCAT('Value ', option_idx), -- 더미 옵션 값
    CAST(RAND() * 5000 + 100 AS DECIMAL(10, 2))
FROM option_data;


-- 총 1,501,500개의 좋아요 데이터 삽입 (10만개 상품 + 100개 브랜드에 평균 15개씩)
-- user_id에 1부터 1만 사이의 랜덤 값을 부여합니다.

INSERT INTO user_like (user_id, target_id, target_type)
WITH RECURSIVE like_data AS (
    SELECT
        1 AS id,
        CAST(RAND() * 9999 + 1 AS UNSIGNED) AS user_id_tmp
    UNION ALL
    SELECT
        id + 1,
        CAST(RAND() * 9999 + 1 AS UNSIGNED)
    FROM like_data
    WHERE id < 1501500
)
SELECT
    user_id_tmp,
    -- 100,000개 상품 또는 100개 브랜드 중 무작위 선택
    CASE WHEN RAND() > 0.99 THEN CAST(RAND() * 99 + 1 AS UNSIGNED) ELSE CAST(RAND() * 99999 + 1 AS UNSIGNED) END AS target_id_tmp,
    CASE WHEN RAND() > 0.99 THEN 'BRAND' ELSE 'PRODUCT' END AS target_type_tmp
FROM like_data;

-- 좋아요 데이터가 삽입된 후 like_summary 테이블 업데이트
INSERT INTO like_summary (like_count, target_id, target_type)
SELECT
    COUNT(*) AS like_count,
    target_id,
    target_type
FROM user_like
GROUP BY target_id, target_type;
