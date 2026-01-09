package org.ormi.priv.tfa.orderflow.productregistry.application;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;

/**
 * Représente une commande liée aux opérations sur les produits dans le registre.
 * <p>
 * Cette interface scellée définit toutes les opérations possibles sur un produit,
 * comme l'enregistrement d'un nouveau produit, la mise hors service d'un produit existant,
 * ou la mise à jour de son nom ou de sa description.
 */


public sealed interface ProductCommand {
    public record RegisterProductCommand(
            String name,
            String description,
            SkuId skuId) implements ProductCommand {
    }

    public record RetireProductCommand(ProductId productId) implements ProductCommand {
    }

    public record UpdateProductNameCommand(ProductId productId, String newName) implements ProductCommand {
    }

    public record UpdateProductDescriptionCommand(ProductId productId, String newDescription) implements ProductCommand {
    }
}
