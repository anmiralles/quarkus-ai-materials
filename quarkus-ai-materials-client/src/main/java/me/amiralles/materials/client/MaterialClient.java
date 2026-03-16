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
import me.amiralles.materials.client.model.Material;
import me.amiralles.materials.client.model.MaterialAttachment;
import me.amiralles.materials.client.model.MaterialCategory;
import me.amiralles.materials.client.model.Product;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/materials")
@RegisterRestClient(configKey = "material-client")
@RegisterProvider(RestClientExceptionMapper.class)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface MaterialClient {

    @GET
    Uni<List<Material>> list(@QueryParam("name") String name,
                             @QueryParam("supplierRef") String supplierRef,
                             @QueryParam("category") MaterialCategory category);

    @GET
    @Path("/{id}")
    Uni<Material> findById(@PathParam("id") Long id);

    @GET
    @Path("/{id}/products")
    Uni<List<Product>> findProducts(@PathParam("id") Long id);

    @POST
    Uni<Material> create(Material material);

    @PUT
    @Path("/{id}")
    Uni<Material> update(@PathParam("id") Long id, Material data);

    @DELETE
    @Path("/{id}")
    Uni<Void> delete(@PathParam("id") Long id);

    @GET
    @Path("/{id}/attachments")
    Uni<List<MaterialAttachment>> listAttachments(@PathParam("id") Long id);

    @POST
    @Path("/{id}/attachments")
    Uni<MaterialAttachment> addAttachment(@PathParam("id") Long id, MaterialAttachment data);

    @GET
    @Path("/{id}/attachments/{attachmentId}")
    Uni<MaterialAttachment> getAttachment(@PathParam("id") Long id, @PathParam("attachmentId") Long attachmentId);

    @DELETE
    @Path("/{id}/attachments/{attachmentId}")
    Uni<Void> deleteAttachment(@PathParam("id") Long id, @PathParam("attachmentId") Long attachmentId);
}
