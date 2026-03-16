package me.amiralles.materials.control;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import me.amiralles.materials.entity.BomStatus;
import me.amiralles.materials.entity.Material;
import me.amiralles.materials.entity.MaterialAttachment;
import me.amiralles.materials.entity.MaterialCategory;
import me.amiralles.materials.entity.Product;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class MaterialService {

    @Inject
    Mutiny.SessionFactory sf;

    public Uni<List<Material>> listAll() {
        return Material.listAll();
    }

    public Uni<List<Material>> findByCategory(MaterialCategory category) {
        return Material.findByCategory(category);
    }

    public Uni<List<Material>> findByName(String name) {
        return Material.findByNameContaining(name);
    }

    public Uni<List<Material>> findBySupplierRef(String supplierRef) {
        return Material.findBySupplierRef(supplierRef);
    }

    public Uni<List<Product>> findProductsUsingMaterial(Long materialId) {
        return sf.withSession(session ->
            session.createQuery(
                "SELECT DISTINCT b.product FROM BomItem bi JOIN bi.bom b " +
                "WHERE bi.material.id = :mid AND b.status = :status", Product.class)
              .setParameter("mid", materialId)
              .setParameter("status", BomStatus.ACTIVE)
              .getResultList());
    }

    public Uni<Material> findById(Long id) {
        return Material.<Material>findById(id)
                .onItem().ifNull().failWith(() ->
                        new NotFoundException("Material " + id + " not found"));
    }

    @WithTransaction
    public Uni<Material> create(Material material) {
        return material.persist();
    }

    @WithTransaction
    public Uni<Material> update(Long id, Material data) {
        return Material.<Material>findById(id)
                .onItem().ifNull().failWith(() ->
                        new NotFoundException("Material " + id + " not found"))
                .onItem().invoke(existing -> {
                    existing.code = data.code;
                    existing.name = data.name;
                    existing.description = data.description;
                    existing.category = data.category;
                    existing.color = data.color;
                    existing.composition = data.composition;
                    existing.supplierRef = data.supplierRef;
                    existing.minStock = data.minStock;
                });
    }

    public Uni<List<MaterialAttachment>> listAttachments(Long materialId) {
        return findById(materialId)
            .flatMap(mat -> MaterialAttachment.findByMaterial(materialId));
    }

    @WithTransaction
    public Uni<MaterialAttachment> addAttachment(Long materialId, MaterialAttachment data) {
        return findById(materialId).flatMap(material -> {
            data.material  = material;
            data.createdAt = LocalDateTime.now();
            return data.persist();
        });
    }

    public Uni<MaterialAttachment> findAttachment(Long materialId, Long attachmentId) {
        return MaterialAttachment.<MaterialAttachment>findById(attachmentId)
            .onItem().ifNull().failWith(() ->
                new NotFoundException("Attachment " + attachmentId + " not found"))
            .onItem().invoke(att -> {
                if (!att.material.id.equals(materialId))
                    throw new NotFoundException("Attachment " + attachmentId + " not found");
            });
    }

    @WithTransaction
    public Uni<Void> deleteAttachment(Long materialId, Long attachmentId) {
        return findAttachment(materialId, attachmentId)
            .flatMap(att -> att.delete())
            .replaceWithVoid();
    }

    @WithTransaction
    public Uni<Void> delete(Long id) {
        return Material.deleteById(id)
                .onItem().invoke(deleted -> {
                    if (!deleted) throw new NotFoundException("Material " + id + " not found");
                }).replaceWithVoid();
    }
}
