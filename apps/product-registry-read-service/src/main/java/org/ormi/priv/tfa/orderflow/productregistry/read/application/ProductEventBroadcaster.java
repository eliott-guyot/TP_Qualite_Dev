package org.ormi.priv.tfa.orderflow.productregistry.read.application;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductStreamElementDto;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.MultiEmitter;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Diffuseur d'événements pour le registre des produits.
 * Permet de diffuser les mises à jour de produits aux abonnés via des flux réactifs.
 */
@ApplicationScoped
public class ProductEventBroadcaster {

    private final CopyOnWriteArrayList<MultiEmitter<? super ProductStreamElementDto>> emitters = new CopyOnWriteArrayList<>();

    public void broadcast(ProductStreamElementDto element) {
        emitters.forEach(emitter -> emitter.emit(element));
    }

    public Multi<ProductStreamElementDto> stream() {
        return Multi.createFrom().emitter(emitter -> {
            emitters.add(emitter);
            // TODO: log a debug, "New emitter added"

            // TODO: Hey! remove emitters, my RAM is melting! (and log for debugging)
            emitter.onTermination(() -> emitters.remove(emitter));
        });
    }

    /**
     * Retourne un flux d'événements filtré pour un produit spécifique.
     *
     * @param productId L'identifiant du produit à surveiller.
     * @return Un Multi émettant les événements liés à ce produit.
     */
    public Multi<ProductStreamElementDto> streamByProductId(String productId) {
        return stream()
                .select().where(e -> e.productId().equals(productId));
    }

    /**
     * Retourne un flux d'événements filtré pour une liste de produits.
     *
     * @param productIds La liste des identifiants de produits à surveiller.
     * @return Un Multi émettant les événements liés à ces produits.
     */
    public Multi<ProductStreamElementDto> streamByProductIds(List<String> productIds) {
        return stream()
                .select().where(e -> productIds.contains(e.productId()));
    }
}