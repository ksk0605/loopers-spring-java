package com.loopers.application.product;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.loopers.application.common.PageInfo;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.inventory.InventoryService;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductCacheRepository;
import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductOptionInfo;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.rank.RankCommand;
import com.loopers.domain.rank.RankService;
import com.loopers.domain.rank.RankedProduct;
import com.loopers.domain.usersignal.TargetLikeCount;
import com.loopers.domain.usersignal.TargetType;
import com.loopers.domain.usersignal.UserSignalService;
import com.loopers.support.annotation.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ProductFacade {
    private final ProductService productService;
    private final InventoryService inventoryService;
    private final BrandService brandService;
    private final LikeService likeService;
    private final ProductCacheRepository productCacheRepository;
    private final UserSignalService userSignalService;
    private final RankService rankService;

    @Transactional(readOnly = true)
    public ProductDetailResult getProduct(Long productId) {
        ProductInfo product = productService.getInfo(productId);
        List<Long> optionIds = product.getOptions().stream()
            .map(ProductOptionInfo::getId)
            .toList();
        Map<Long, Integer> stockQuantities = inventoryService.getStockQuantities(optionIds);
        Brand brand = brandService.get(product.getBrandId());
        var likeCount = likeService.count(productId, LikeTargetType.PRODUCT);
        return ProductDetailResult.of(product, brand, likeCount, stockQuantities);
    }

    @Transactional(readOnly = true)
    public ProductResults getProducts(ProductCommand.Search command) {
        Optional<ProductResults> results = productCacheRepository.getProductResults(command);
        if (results.isPresent()) {
            return results.get();
        }
        var products = productService.getAll(command);
        List<Long> brandIds = products.getContent().stream()
            .map(product -> product.getBrandId())
            .toList();
        var brands = brandService.getAll(brandIds);

        var pageInfo = new PageInfo(
            products.getNumber(),
            products.getSize(),
            products.getTotalPages(),
            products.getTotalElements(),
            products.hasNext());

        List<Long> productIds = products.getContent().stream()
            .map(product -> product.getId())
            .toList();
        List<TargetLikeCount> targetLikeCounts = userSignalService.getTargetLikeCountsIn(productIds, TargetType.PRODUCT);
        ProductResults productResults = ProductResults.of(products, brands, targetLikeCounts, pageInfo);
        productCacheRepository.setProductResults(command, productResults);
        return productResults;
    }

    @Transactional(readOnly = true)
    public ProductResults getDailyRanking(RankCommand.Get command) {
        List<RankedProduct> rankedProducts = rankService.getRankRangeWithScores(command);
        List<Long> productIds = rankedProducts.stream()
            .map(RankedProduct::productId)
            .toList();
        var products = productService.getAll(productIds);
        List<Long> brandIds = products.stream()
            .map(Product::getBrandId)
            .toList();
        var brands = brandService.getAll(brandIds);

        Long totalElements = rankService.getTotalSize(command.date());
        var pageInfo = new PageInfo(
            command.page(),
            command.size(),
            (int)Math.ceil((double)totalElements / command.size()),
            totalElements,
            ((long)command.page() * command.size()) < totalElements);

        List<TargetLikeCount> targetLikeCounts = userSignalService.getTargetLikeCountsIn(productIds, TargetType.PRODUCT);
        return ProductResults.of(products, brands, targetLikeCounts, pageInfo);
    }

    @Transactional(readOnly = true)
    public ProductResults getRanking(RankCommand.GetV2 command) {
        List<RankedProduct> rankedProducts = rankService.getRankRangeWithScores(command);
        List<Long> productIds = rankedProducts.stream()
            .map(RankedProduct::productId)
            .toList();
        var products = productService.getAll(productIds);
        List<Long> brandIds = products.stream()
            .map(Product::getBrandId)
            .toList();
        var brands = brandService.getAll(brandIds);

        Long totalElements = rankService.getTotalSize(command.period());
        var pageInfo = new PageInfo(
            command.page(),
            command.size(),
            (int)Math.ceil((double)totalElements / command.size()),
            totalElements,
            ((long)command.page() * command.size()) < totalElements);

        List<TargetLikeCount> targetLikeCounts = userSignalService.getTargetLikeCountsIn(productIds, TargetType.PRODUCT);
        return ProductResults.of(products, brands, targetLikeCounts, pageInfo);
    }
}
