package me.amiralles.materials.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product extends PanacheEntity {

    @Column(nullable = false, unique = true)
    public String sku;

    @Column(nullable = false)
    public String name;

    public String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public ProductCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public ProductStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    public List<Bom> boms = new ArrayList<>();

    // --- Custom queries ---

    public static Uni<Product> findBySku(String sku) {
        return find("sku", sku).firstResult();
    }

    public static Uni<List<Product>> findByStatus(ProductStatus status) {
        return list("status", status);
    }

    public static Uni<List<Product>> findByCategory(ProductCategory category) {
        return list("category", category);
    }

    public static Uni<List<Product>> findByStatusAndCategory(ProductStatus status, ProductCategory category) {
        return list("status = ?1 and category = ?2", status, category);
    }
}
