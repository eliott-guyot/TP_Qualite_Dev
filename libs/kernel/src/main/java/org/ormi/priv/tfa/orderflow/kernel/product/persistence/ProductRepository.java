package org.ormi.priv.tfa.orderflow.kernel.product.persistence;

import java.util.Optional;

import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;

/**
 * Repository pour la persistance de l'agrégat Product (partie commande/écriture). Permet la sauvegarde et la récupération de l'état actuel des produits.
 */

public interface ProductRepository {
    void save(Product product);
    Optional<Product> findById(ProductId id);
    boolean existsBySkuId(SkuId skuId);
}
