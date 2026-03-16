package me.amiralles.materials.client;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
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
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/products")
@RegisterRestClient(configKey = "product-client")
@RegisterProvider(RestClientExceptionMapper.class)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ProductClient {

    @GET
    Uni<List<Product>> list(@QueryParam("sku") String sku,
                            @QueryParam("status") ProductStatus status,
                            @QueryParam("category") ProductCategory category);

    @GET
    @Path("/{id}")
    Uni<Product> findById(@PathParam("id") Long id);

    @GET
    @Path("/{id}/materials")
    Uni<List<Material>> findMaterials(@PathParam("id") Long id);

    @GET
    @Path("/{id}/boms")
    Uni<List<Bom>> listBoms(@PathParam("id") Long id, @QueryParam("status") BomStatus status);

    @GET
    @Path("/{id}/boms/active")
    Uni<Bom> getActiveBom(@PathParam("id") Long id);

    @POST
    Uni<Product> create(Product product);

    @PUT
    @Path("/{id}")
    Uni<Product> update(@PathParam("id") Long id, Product data);

    @DELETE
    @Path("/{id}")
    Uni<Void> delete(@PathParam("id") Long id);
}
