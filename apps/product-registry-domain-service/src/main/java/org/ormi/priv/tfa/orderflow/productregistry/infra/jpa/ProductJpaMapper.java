package org.ormi.priv.tfa.orderflow.productregistry.infra.jpa;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuIdMapper;

/**
 * Mapper MapStruct pour convertir entre l'entité JPA {@link ProductEntity} et l'objet domaine {@link Product}.
 * <p>
 * Ce mapper permet de :
 * <ul>
 *   <li>Convertir une entité JPA en objet domaine (`toDomain`)</li>
 *   <li>Mettre à jour une entité existante à partir d'un objet domaine (`updateEntity`)</li>
 *   <li>Convertir un objet domaine en entité JPA (`toEntity`)</li>
 * </ul>
 * <p>
 * Il utilise les mappers {@link ProductIdMapper} et {@link SkuIdMapper} pour gérer les conversions
 * spécifiques des identifiants et SKU.
 */

@Mapper(
    componentModel = "cdi",
    builder = @Builder(disableBuilder = false),
    uses = { ProductIdMapper.class, SkuIdMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ProductJpaMapper {

    public abstract Product toDomain(ProductEntity entity);

    public abstract void updateEntity(Product product, @MappingTarget ProductEntity entity);

    public abstract ProductEntity toEntity(Product product);
}
