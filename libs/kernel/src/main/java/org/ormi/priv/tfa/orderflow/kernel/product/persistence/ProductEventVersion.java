package org.ormi.priv.tfa.orderflow.kernel.product.persistence;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1;

/**
 * Enumération des versions supportées pour les événements produits, permettant la gestion de l'évolution du schéma des événements (Event Versioning).
 */

public enum ProductEventVersion {
    V1(ProductEventV1.EVENT_VERSION);

    private final int value;

    ProductEventVersion(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
