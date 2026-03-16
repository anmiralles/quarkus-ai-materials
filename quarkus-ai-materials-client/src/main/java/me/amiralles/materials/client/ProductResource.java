package me.amiralles.materials.client;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import me.amiralles.materials.client.model.Bom;
import me.amiralles.materials.client.model.BomStatus;
import me.amiralles.materials.client.model.Material;
import me.amiralles.materials.client.model.Product;
import me.amiralles.materials.client.model.ProductCategory;
import me.amiralles.materials.client.model.ProductStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    @RestClient
    ProductClient productClient;

    @GET
    public Uni<List<Product>> list(@QueryParam("sku") String sku,
                                   @QueryParam("status") ProductStatus status,
                                   @QueryParam("category") ProductCategory category) {
        return productClient.list(sku, status, category);
    }

    @GET
    @Path("/{id}")
    public Uni<Product> findById(@PathParam("id") Long id) {
        return productClient.findById(id);
    }

    @GET
    @Path("/{id}/materials")
    public Uni<List<Material>> findMaterials(@PathParam("id") Long id) {
        return productClient.findMaterials(id);
    }

    @GET
    @Path("/{id}/boms")
    public Uni<List<Bom>> listBoms(@PathParam("id") Long id, @QueryParam("status") BomStatus status) {
        return productClient.listBoms(id, status);
    }

    @GET
    @Path("/{id}/boms/active")
    public Uni<Bom> getActiveBom(@PathParam("id") Long id) {
        return productClient.getActiveBom(id);
    }
}
