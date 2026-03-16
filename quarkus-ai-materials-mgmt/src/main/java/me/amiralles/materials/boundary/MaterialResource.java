package me.amiralles.materials.boundary;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
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
import me.amiralles.materials.control.MaterialService;
import me.amiralles.materials.entity.Material;
import me.amiralles.materials.entity.MaterialAttachment;
import me.amiralles.materials.entity.MaterialCategory;
import me.amiralles.materials.entity.Product;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaterialResource {

    @Inject
    MaterialService service;

    @GET
    public Uni<RestResponse<List<Material>>> list(
            @QueryParam("name") String name,
            @QueryParam("supplierRef") String supplierRef,
            @QueryParam("category") MaterialCategory category) {
        Uni<List<Material>> result;
        if (name != null) {
            result = service.findByName(name);
        } else if (supplierRef != null) {
            result = service.findBySupplierRef(supplierRef);
        } else if (category != null) {
            result = service.findByCategory(category);
        } else {
            result = service.listAll();
        }
        return result.map(RestResponse::ok);
    }

    @GET
    @Path("/{id}")
    public Uni<RestResponse<Material>> findById(@PathParam("id") Long id) {
        return service.findById(id).map(RestResponse::ok);
    }

    @GET
    @Path("/{id}/products")
    public Uni<RestResponse<List<Product>>> findProducts(@PathParam("id") Long id) {
        return service.findProductsUsingMaterial(id).map(RestResponse::ok);
    }

    @POST
    public Uni<RestResponse<Material>> create(Material material) {
        return service.create(material)
                .map(created -> RestResponse.status(RestResponse.Status.CREATED, created));
    }

    @PUT
    @Path("/{id}")
    public Uni<RestResponse<Material>> update(@PathParam("id") Long id, Material data) {
        return service.update(id, data).map(RestResponse::ok);
    }

    @DELETE
    @Path("/{id}")
    public Uni<RestResponse<Void>> delete(@PathParam("id") Long id) {
        return service.delete(id).map(ignored -> RestResponse.noContent());
    }

    @GET
    @Path("/{id}/attachments")
    public Uni<RestResponse<List<MaterialAttachment>>> listAttachments(
            @PathParam("id") Long id) {
        return service.listAttachments(id).map(RestResponse::ok);
    }

    @POST
    @Path("/{id}/attachments")
    public Uni<RestResponse<MaterialAttachment>> addAttachment(
            @PathParam("id") Long id, MaterialAttachment data) {
        return service.addAttachment(id, data)
            .map(att -> RestResponse.status(RestResponse.Status.CREATED, att));
    }

    @GET
    @Path("/{id}/attachments/{attachmentId}")
    public Uni<RestResponse<MaterialAttachment>> getAttachment(
            @PathParam("id") Long id, @PathParam("attachmentId") Long attachmentId) {
        return service.findAttachment(id, attachmentId).map(RestResponse::ok);
    }

    @DELETE
    @Path("/{id}/attachments/{attachmentId}")
    public Uni<RestResponse<Void>> deleteAttachment(
            @PathParam("id") Long id, @PathParam("attachmentId") Long attachmentId) {
        return service.deleteAttachment(id, attachmentId)
            .map(ignored -> RestResponse.noContent());
    }
}
