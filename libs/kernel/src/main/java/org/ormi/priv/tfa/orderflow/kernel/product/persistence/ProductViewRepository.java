package org.ormi.priv.tfa.orderflow.kernel.product.persistence;

import java.util.List;
import java.util.Optional;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView;

/**
 * Repository pour la lecture et la recherche des vues de produits (Read Model). Offre des fonctionnalités de recherche paginée.
 */

public interface ProductViewRepository {
    void save(ProductView productView);
    Optional<ProductView> findById(ProductId id);
    Optional<ProductView> findBySkuId(SkuId skuId);
    long countPaginatedViewsBySkuIdPattern(String skuIdPattern);
    List<ProductView> searchPaginatedViewsOrderBySkuId(String skuIdPattern, int page, int size);
}
