package me.amiralles.materials.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "material_attachment")
public class MaterialAttachment extends PanacheEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    public Material material;

    @Column(nullable = false, length = 2048)
    public String url;

    @Column(name = "file_name", length = 512)
    public String fileName;

    @Column(name = "content_type")
    public String contentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "attachment_type", nullable = false, length = 50)
    public AttachmentType attachmentType;

    public String description;

    @Column(name = "created_at", nullable = false)
    public LocalDateTime createdAt;

    // --- Custom queries ---

    public static Uni<List<MaterialAttachment>> findByMaterial(Long materialId) {
        return list("material.id", materialId);
    }

    public static Uni<List<MaterialAttachment>> findByMaterialAndType(
            Long materialId, AttachmentType type) {
        return list("material.id = ?1 and attachmentType = ?2", materialId, type);
    }
}
