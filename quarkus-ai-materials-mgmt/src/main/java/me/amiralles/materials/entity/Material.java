package me.amiralles.materials.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "material")
public class Material extends PanacheEntity {

    @Column(nullable = false, unique = true)
    public String code;

    @Column(nullable = false)
    public String name;

    public String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public MaterialCategory category;

    public String color;

    /** Fiber blend, e.g. "60% cotton, 40% polyester" */
    public String composition;

    @Column(name = "supplier_ref")
    public String supplierRef;

    @Column(name = "min_stock", precision = 12, scale = 2)
    public BigDecimal minStock;

    @JsonIgnore
    @OneToMany(mappedBy = "material")
    public List<MaterialAttachment> attachments = new ArrayList<>();

    // --- Custom queries ---

    public static Uni<Material> findByCode(String code) {
        return find("code", code).firstResult();
    }

    public static Uni<List<Material>> findByCategory(MaterialCategory category) {
        return list("category", category);
    }

    public static Uni<List<Material>> findByNameContaining(String name) {
        return list("lower(name) like lower(?1)", "%" + name + "%");
    }

    public static Uni<List<Material>> findBySupplierRef(String supplierRef) {
        return list("supplierRef", supplierRef);
    }
}
