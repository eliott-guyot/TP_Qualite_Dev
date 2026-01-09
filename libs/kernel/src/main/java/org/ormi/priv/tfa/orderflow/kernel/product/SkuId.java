package org.ormi.priv.tfa.orderflow.kernel.product;

import jakarta.validation.constraints.NotNull;

/**
 * Value Object représentant le SKU (Stock Keeping Unit) d'un produit. Assure la validation du format (XXX-00000).
 */

public record SkuId(@NotNull String value) {
    private static final java.util.regex.Pattern SKU_PATTERN =
        java.util.regex.Pattern.compile("^[A-Z]{3}-\\d{5}$");

    public SkuId {
        if (!SKU_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid SKU format, expected [Alpha]{3}-[Digit]{5}");
        }
    }
}
