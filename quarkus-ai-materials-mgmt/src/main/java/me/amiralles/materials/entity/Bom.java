package me.amiralles.materials.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bom", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_id", "version"})
})
public class Bom extends PanacheEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    public Product product;

    @Column(nullable = false)
    public Integer version;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public BomStatus status;

    @Column(name = "effective_date")
    public LocalDate effectiveDate;

    @JsonIgnore
    @OneToMany(mappedBy = "bom", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<BomItem> items = new ArrayList<>();

    // --- Custom queries ---

    public static Uni<List<Bom>> findByProduct(Long productId) {
        return list("product.id", productId);
    }

    public static Uni<Bom> findActiveByProduct(Long productId) {
        return find("product.id = ?1 and status = ?2", productId, BomStatus.ACTIVE)
                .firstResult();
    }

    /** Fetch a BOM with all its items eagerly loaded (avoids LazyInitializationException) */
    public static Uni<Bom> findByIdWithItems(Long bomId) {
        return find("select b from Bom b left join fetch b.items where b.id = ?1", bomId)
                .firstResult();
    }

    public static Uni<List<Bom>> findByProductAndStatus(Long productId, BomStatus status) {
        return list("product.id = ?1 and status = ?2", productId, status);
    }
}
