package org.ormi.priv.tfa.orderflow.productregistry.application;

import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.EventLogEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.OutboxEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.EventLogRepository;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.OutboxRepository;
import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductDescriptionUpdated;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductNameUpdated;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductRepository;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.UpdateProductDescriptionCommand;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.UpdateProductNameCommand;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;


/**
 * Service d'application chargé de mettre à jour les informations d'un produit dans le registre.
 * <p>
 * Ce service permet de modifier le nom ou la description d'un produit existant.
 * Pour chaque mise à jour, il récupère le produit par son identifiant, applique la modification,
 * sauvegarde les changements dans le dépôt, enregistre l'événement correspondant dans le journal
 * des événements et publie l'événement dans l'outbox pour la communication avec d'autres systèmes.
 */


@ApplicationScoped
public class UpdateProductService {

    ProductRepository repository;
    EventLogRepository eventLog;
    OutboxRepository outbox;

    @Inject
    public UpdateProductService(
        ProductRepository repository,
        EventLogRepository eventLog,
        OutboxRepository outbox
    ) {
        this.repository = repository;
        this.eventLog = eventLog;
        this.outbox = outbox;
    }

    @Transactional
    public void handle(UpdateProductNameCommand cmd) throws IllegalArgumentException {
        Product product = repository.findById(cmd.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        EventEnvelope<ProductNameUpdated> event = product.updateName(cmd.newName());
        // Save domain object
        repository.save(product);
        // Append event to event log
        final EventLogEntity persistedEvent = eventLog.append(event);
        // Publish event to outbox
        outbox.publish(
            OutboxEntity.Builder()
                .sourceEvent(persistedEvent)
                .build()
        );
    }

    @Transactional
    public void handle(UpdateProductDescriptionCommand cmd) throws IllegalArgumentException {
        Product product = repository.findById(cmd.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        EventEnvelope<ProductDescriptionUpdated> event = product.updateDescription(cmd.newDescription());
        // Save domain object
        repository.save(product);
        // Append event to event log
        final EventLogEntity persistedEvent = eventLog.append(event);
        // Publish event to outbox
        outbox.publish(
            OutboxEntity.Builder()
                .sourceEvent(persistedEvent)
                .build()
        );
    }
}
