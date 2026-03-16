package me.amiralles.materials.client;

import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkus.runtime.Startup;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.amiralles.materials.client.model.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class MaterialsMcpTools {

    private static final Logger LOGGER = Logger.getLogger(MaterialsMcpTools.class);

    @Inject
    @ConfigProperty(name = "server.url")
    String baseUrl;

    @Inject
    MaterialResource materialResource;

    @Inject
    ProductResource productResource;

    @Inject
    BomResource bomResource;

    @Startup
    void init() {
        LOGGER.info("Starting Materials client MCP server, application URL: " + baseUrl);
    }

    // --- Material tools ---

    @Tool(description = "Lists materials, optionally filtered by name, supplier reference, or category. Category values: FABRIC, THREAD, BUTTON, ZIPPER, LABEL, LINING, INTERLINING, ELASTIC, PACKAGING")
    public Uni<List<Material>> list_materials(
            @ToolArg(description = "Filter by material name (partial match)", required = false) String name,
            @ToolArg(description = "Filter by supplier reference code", required = false) String supplierRef,
            @ToolArg(description = "Filter by category: FABRIC, THREAD, BUTTON, ZIPPER, LABEL, LINING, INTERLINING, ELASTIC, PACKAGING", required = false) MaterialCategory category) {
        LOGGER.infof("MCP Tool: Listing materials name=%s supplierRef=%s category=%s", name, supplierRef, category);
        return materialResource.list(name, supplierRef, category)
                .onFailure().invoke(t -> LOGGER.errorf(t, "list_materials failed: %s", t.getMessage()));
    }

    @Tool(description = "Gets a material by its ID")
    public Uni<Material> get_material_by_id(
            @ToolArg(description = "The ID of the material", required = true) Long id) {
        LOGGER.infof("MCP Tool: Getting material by id=%d", id);
        return materialResource.findById(id)
                .onFailure().invoke(t -> LOGGER.errorf(t, "get_material_by_id failed: %s", t.getMessage()));
    }

    @Tool(description = "Lists all products that use a given material")
    public Uni<List<Product>> get_products_by_material(
            @ToolArg(description = "The ID of the material", required = true) Long id) {
        LOGGER.infof("MCP Tool: Getting products for material id=%d", id);
        return materialResource.findProducts(id)
                .onFailure().invoke(t -> LOGGER.errorf(t, "get_products_by_material failed: %s", t.getMessage()));
    }

    @Tool(description = "Lists all file attachments for a material")
    public Uni<List<MaterialAttachment>> list_material_attachments(
            @ToolArg(description = "The ID of the material", required = true) Long id) {
        LOGGER.infof("MCP Tool: Listing attachments for material id=%d", id);
        return materialResource.listAttachments(id)
                .onFailure().invoke(t -> LOGGER.errorf(t, "list_material_attachments failed: %s", t.getMessage()));
    }

    @Tool(description = "Gets a specific attachment for a material by attachment ID")
    public Uni<MaterialAttachment> get_material_attachment(
            @ToolArg(description = "The ID of the material", required = true) Long id,
            @ToolArg(description = "The ID of the attachment", required = true) Long attachmentId) {
        LOGGER.infof("MCP Tool: Getting attachment id=%d for material id=%d", attachmentId, id);
        return materialResource.getAttachment(id, attachmentId)
                .onFailure().invoke(t -> LOGGER.errorf(t, "get_material_attachment failed: %s", t.getMessage()));
    }

    // --- Product tools ---

    @Tool(description = "Lists products, optionally filtered by SKU, status, or category. Status values: DEVELOPMENT, ACTIVE, DISCONTINUED. Category values: SHIRT, PANTS, JACKET, DRESS, ACCESSORY, HOME_TEXTILE")
    public Uni<List<Product>> list_products(
            @ToolArg(description = "Filter by SKU (exact match)", required = false) String sku,
            @ToolArg(description = "Filter by status: DEVELOPMENT, ACTIVE, DISCONTINUED", required = false) ProductStatus status,
            @ToolArg(description = "Filter by category: SHIRT, PANTS, JACKET, DRESS, ACCESSORY, HOME_TEXTILE", required = false) ProductCategory category) {
        LOGGER.infof("MCP Tool: Listing products sku=%s status=%s category=%s", sku, status, category);
        return productResource.list(sku, status, category)
                .onFailure().invoke(t -> LOGGER.errorf(t, "list_products failed: %s", t.getMessage()));
    }

    @Tool(description = "Gets a product by its ID")
    public Uni<Product> get_product_by_id(
            @ToolArg(description = "The ID of the product", required = true) Long id) {
        LOGGER.infof("MCP Tool: Getting product by id=%d", id);
        return productResource.findById(id)
                .onFailure().invoke(t -> LOGGER.errorf(t, "get_product_by_id failed: %s", t.getMessage()));
    }

    @Tool(description = "Lists all materials used in a product across its BOMs")
    public Uni<List<Material>> get_materials_by_product(
            @ToolArg(description = "The ID of the product", required = true) Long id) {
        LOGGER.infof("MCP Tool: Getting materials for product id=%d", id);
        return productResource.findMaterials(id)
                .onFailure().invoke(t -> LOGGER.errorf(t, "get_materials_by_product failed: %s", t.getMessage()));
    }

    @Tool(description = "Lists all BOMs for a product, optionally filtered by status. Status values: DRAFT, ACTIVE, OBSOLETE")
    public Uni<List<Bom>> list_product_boms(
            @ToolArg(description = "The ID of the product", required = true) Long id,
            @ToolArg(description = "Filter by BOM status: DRAFT, ACTIVE, OBSOLETE", required = false) BomStatus status) {
        LOGGER.infof("MCP Tool: Listing BOMs for product id=%d status=%s", id, status);
        return productResource.listBoms(id, status)
                .onFailure().invoke(t -> LOGGER.errorf(t, "list_product_boms failed: %s", t.getMessage()));
    }

    @Tool(description = "Gets the currently active BOM for a product")
    public Uni<Bom> get_active_bom_for_product(
            @ToolArg(description = "The ID of the product", required = true) Long id) {
        LOGGER.infof("MCP Tool: Getting active BOM for product id=%d", id);
        return productResource.getActiveBom(id)
                .onFailure().invoke(t -> LOGGER.errorf(t, "get_active_bom_for_product failed: %s", t.getMessage()));
    }

    // --- BOM tools ---

    @Tool(description = "Lists BOMs, optionally filtered by product ID and/or status. Status values: DRAFT, ACTIVE, OBSOLETE")
    public Uni<List<Bom>> list_boms(
            @ToolArg(description = "Filter by product ID", required = false) Long productId,
            @ToolArg(description = "Filter by status: DRAFT, ACTIVE, OBSOLETE", required = false) BomStatus status) {
        LOGGER.infof("MCP Tool: Listing BOMs productId=%d status=%s", productId, status);
        return bomResource.list(productId, status)
                .onFailure().invoke(t -> LOGGER.errorf(t, "list_boms failed: %s", t.getMessage()));
    }

    @Tool(description = "Gets the active BOM for a given product")
    public Uni<Bom> get_active_bom(
            @ToolArg(description = "The product ID to find the active BOM for", required = true) Long productId) {
        LOGGER.infof("MCP Tool: Getting active BOM for productId=%d", productId);
        return bomResource.findActive(productId)
                .onFailure().invoke(t -> LOGGER.errorf(t, "get_active_bom failed: %s", t.getMessage()));
    }

    @Tool(description = "Gets a BOM by its ID")
    public Uni<Bom> get_bom_by_id(
            @ToolArg(description = "The ID of the BOM", required = true) Long id) {
        LOGGER.infof("MCP Tool: Getting BOM by id=%d", id);
        return bomResource.findById(id)
                .onFailure().invoke(t -> LOGGER.errorf(t, "get_bom_by_id failed: %s", t.getMessage()));
    }

    @Tool(description = "Lists all BOM items (materials with quantities) for a given BOM")
    public Uni<List<BomItem>> list_bom_items(
            @ToolArg(description = "The ID of the BOM", required = true) Long id) {
        LOGGER.infof("MCP Tool: Listing items for BOM id=%d", id);
        return bomResource.findItems(id)
                .onFailure().invoke(t -> LOGGER.errorf(t, "list_bom_items failed: %s", t.getMessage()));
    }
}
