package me.amiralles.materials.control;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import me.amiralles.materials.entity.Bom;
import me.amiralles.materials.entity.BomItem;
import me.amiralles.materials.entity.BomStatus;
import me.amiralles.materials.entity.Product;

import java.util.List;

@ApplicationScoped
public class BomService {

    public Uni<List<Bom>> findByProduct(Long productId) {
        return Bom.findByProduct(productId);
    }

    public Uni<Bom> findActiveByProduct(Long productId) {
        return Bom.findActiveByProduct(productId)
                .onItem().ifNull().failWith(() ->
                        new NotFoundException("No active BOM for product " + productId));
    }

    public Uni<Bom> findById(Long id) {
        return Bom.findByIdWithItems(id)
                .onItem().ifNull().failWith(() ->
                        new NotFoundException("BOM " + id + " not found"));
    }

    public Uni<List<BomItem>> findItemsByBom(Long bomId) {
        return BomItem.findByBomWithMaterial(bomId);
    }

    @WithTransaction
    public Uni<Bom> create(Long productId, Bom bom) {
        return Product.<Product>findById(productId)
                .onItem().ifNull().failWith(() ->
                        new NotFoundException("Product " + productId + " not found"))
                .onItem().transformToUni(product -> {
                    bom.product = product;
                    return bom.persist();
                });
    }

    @WithTransaction
    public Uni<Bom> update(Long id, Bom data) {
        return Bom.<Bom>findById(id)
                .onItem().ifNull().failWith(() ->
                        new NotFoundException("BOM " + id + " not found"))
                .onItem().invoke(existing -> {
                    existing.version = data.version;
                    existing.status = data.status;
                    existing.effectiveDate = data.effectiveDate;
                });
    }

    public Uni<List<Bom>> findByProductAndStatus(Long productId, BomStatus status) {
        return Bom.findByProductAndStatus(productId, status);
    }

    @WithTransaction
    public Uni<Void> delete(Long id) {
        return Bom.deleteById(id)
                .onItem().invoke(deleted -> {
                    if (!deleted) throw new NotFoundException("BOM " + id + " not found");
                }).replaceWithVoid();
    }
}
