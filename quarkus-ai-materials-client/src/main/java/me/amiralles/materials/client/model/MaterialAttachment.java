package me.amiralles.materials.client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

public class MaterialAttachment {

    public Long id;

    @JsonIgnore
    public Material material;

    public String url;

    public String fileName;

    public String contentType;

    public AttachmentType attachmentType;

    public String description;

    public LocalDateTime createdAt;

}
