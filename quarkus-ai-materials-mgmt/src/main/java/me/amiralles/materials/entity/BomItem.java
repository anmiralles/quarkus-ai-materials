package me.amiralles.materials.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "bom_item")
public class BomItem extends PanacheEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    public Bom bom;

    @ManyToOne(fetch = FetchType.LAZY)
    public Material material;

    @Column(nullable = false, precision = 12, scale = 4)
    public BigDecimal quantity;

    /** Cutting/production waste percentage (e.g. 5.0 means 5%) */
    @Column(name = "waste_pct", precision = 5, scale = 2)
    public BigDecimal wastePct;

    public String notes;

    // --- Derived ---

    /** Effective quantity including waste: quantity * (1 + wastePct/100) */
    public BigDecimal effectiveQuantity() {
        if (wastePct == null || wastePct.compareTo(BigDecimal.ZERO) == 0) {
            return quantity;
        }
        return quantity.multiply(BigDecimal.ONE.add(wastePct.divide(BigDecimal.valueOf(100))));
    }

    // --- Custom queries ---

    public static Uni<List<BomItem>> findByBom(Long bomId) {
        return list("bom.id", bomId);
    }

    /** Find all BOMs that use a given material (useful for impact analysis) */
    public static Uni<List<BomItem>> findByMaterial(Long materialId) {
        return list("material.id", materialId);
    }

    /** Fetch items for a BOM with material eagerly loaded (avoids LazyInitializationException) */
    public static Uni<List<BomItem>> findByBomWithMaterial(Long bomId) {
        return find("SELECT bi FROM BomItem bi JOIN FETCH bi.material WHERE bi.bom.id = ?1", bomId).list();
    }
}
