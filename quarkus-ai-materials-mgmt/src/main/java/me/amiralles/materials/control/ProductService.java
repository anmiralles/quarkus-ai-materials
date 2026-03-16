package me.amiralles.materials.control;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import me.amiralles.materials.entity.BomStatus;
import me.amiralles.materials.entity.Material;
import me.amiralles.materials.entity.Product;
import me.amiralles.materials.entity.ProductCategory;
import me.amiralles.materials.entity.ProductStatus;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

@ApplicationScoped
public class ProductService {

    @Inject
    Mutiny.SessionFactory sf;

    public Uni<List<Product>> listAll() {
        return Product.listAll();
    }

    public Uni<List<Product>> findByStatus(ProductStatus status) {
        return Product.findByStatus(status);
    }

    public Uni<List<Product>> findByCategory(ProductCategory category) {
        return Product.findByCategory(category);
    }

    public Uni<Product> findBySku(String sku) {
        return Product.findBySku(sku)
            .onItem().ifNull().failWith(() -> new NotFoundException("Product " + sku + " not found"));
    }

    public Uni<List<Product>> findByStatusAndCategory(ProductStatus status, ProductCategory category) {
        return Product.findByStatusAndCategory(status, category);
    }

    public Uni<List<Material>> findMaterialsForProduct(Long productId) {
        return sf.withSession(session ->
            session.createQuery(
                "SELECT bi.material FROM BomItem bi JOIN bi.bom b " +
                "WHERE b.product.id = :pid AND b.status = :status", Material.class)
              .setParameter("pid", productId)
              .setParameter("status", BomStatus.ACTIVE)
              .getResultList());
    }

    public Uni<Product> findById(Long id) {
        return Product.<Product>findById(id)
                .onItem().ifNull().failWith(() ->
                        new NotFoundException("Product " + id + " not found"));
    }

    @WithTransaction
    public Uni<Product> create(Product product) {
        return product.persist();
    }

    @WithTransaction
    public Uni<Product> update(Long id, Product data) {
        return Product.<Product>findById(id)
                .onItem().ifNull().failWith(() ->
                        new NotFoundException("Product " + id + " not found"))
                .onItem().invoke(existing -> {
                    existing.sku = data.sku;
                    existing.name = data.name;
                    existing.description = data.description;
                    existing.category = data.category;
                    existing.status = data.status;
                });
    }

    @WithTransaction
    public Uni<Void> delete(Long id) {
        return Product.deleteById(id)
                .onItem().invoke(deleted -> {
                    if (!deleted) throw new NotFoundException("Product " + id + " not found");
                }).replaceWithVoid();
    }
}
