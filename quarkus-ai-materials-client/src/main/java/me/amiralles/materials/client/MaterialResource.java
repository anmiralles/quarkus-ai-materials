package me.amiralles.materials.client;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import me.amiralles.materials.client.model.Material;
import me.amiralles.materials.client.model.MaterialAttachment;
import me.amiralles.materials.client.model.MaterialCategory;
import me.amiralles.materials.client.model.Product;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Path("/materials")
@Produces(MediaType.APPLICATION_JSON)
public class MaterialResource {

    @Inject
    @RestClient
    MaterialClient materialClient;

    @GET
    public Uni<List<Material>> list(@QueryParam("name") String name,
                                    @QueryParam("supplierRef") String supplierRef,
                                    @QueryParam("category") MaterialCategory category) {
        return materialClient.list(name, supplierRef, category);
    }

    @GET
    @Path("/{id}")
    public Uni<Material> findById(@PathParam("id") Long id) {
        return materialClient.findById(id);
    }

    @GET
    @Path("/{id}/products")
    public Uni<List<Product>> findProducts(@PathParam("id") Long id) {
        return materialClient.findProducts(id);
    }

    @GET
    @Path("/{id}/attachments")
    public Uni<List<MaterialAttachment>> listAttachments(@PathParam("id") Long id) {
        return materialClient.listAttachments(id);
    }

    @GET
    @Path("/{id}/attachments/{attachmentId}")
    public Uni<MaterialAttachment> getAttachment(@PathParam("id") Long id,
                                                 @PathParam("attachmentId") Long attachmentId) {
        return materialClient.getAttachment(id, attachmentId);
    }
}
