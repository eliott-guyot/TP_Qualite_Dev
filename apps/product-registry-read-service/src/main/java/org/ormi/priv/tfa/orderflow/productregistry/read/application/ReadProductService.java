package org.ormi.priv.tfa.orderflow.productregistry.read.application;

import java.util.List;
import java.util.Optional;

import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductStreamElementDto;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductViewRepository;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView;

import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Service de lecture pour le registre des produits.
 * Permet de rechercher des produits et de s'abonner aux événements de mise à jour.
 */
@ApplicationScoped
public class ReadProductService {

    private final ProductViewRepository repository;
    private final ProductEventBroadcaster productEventBroadcaster;

    @Inject
    public ReadProductService(
        ProductViewRepository repository,
        ProductEventBroadcaster productEventBroadcaster) {
        this.repository = repository;
        this.productEventBroadcaster = productEventBroadcaster;
    }

    public Optional<ProductView> findById(ProductId productId) {
        return repository.findById(productId);
    }

    public Optional<ProductView> handle(ProductQuery.GetProductByIdQuery query) {
        return findById(query.productId());
    }

    public SearchPaginatedResult searchProducts(String skuIdPattern, int page, int size) {
        return new SearchPaginatedResult(
                repository.searchPaginatedViewsOrderBySkuId(skuIdPattern, page, size),
                repository.countPaginatedViewsBySkuIdPattern(skuIdPattern));
    }

    public SearchPaginatedResult handle(ProductQuery.ListProductQuery query) {
        return searchProducts("", query.page(), query.size());
    }

    public SearchPaginatedResult handle(ProductQuery.ListProductBySkuIdPatternQuery query) {
        return searchProducts(query.skuIdPattern(), query.page(), query.size());
    }

    public Multi<ProductStreamElementDto> streamProductEvents(ProductId productId) {
        return productEventBroadcaster.streamByProductId(productId.value().toString());
    }

    public Multi<ProductStreamElementDto> streamProductListEvents(String skuIdPattern, int page, int size) {
        final List<ProductView> products = searchProducts(skuIdPattern, page, size).page();
        final List<String> productIds = products.stream()
                .map(p -> p.getId().value().toString())
                .toList();
        return productEventBroadcaster.streamByProductIds(productIds);
    }

    public record SearchPaginatedResult(List<ProductView> page, long total) {
    }
}