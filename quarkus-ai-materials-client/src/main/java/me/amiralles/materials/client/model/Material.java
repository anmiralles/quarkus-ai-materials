package me.amiralles.materials.client.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Material {

    public Long id;

    public String code;

    public String name;

    public String description;

    public MaterialCategory category;

    public String color;

    public String composition;

    public String supplierRef;

    public BigDecimal minStock;

    public List<MaterialAttachment> attachments = new ArrayList<>();

}
