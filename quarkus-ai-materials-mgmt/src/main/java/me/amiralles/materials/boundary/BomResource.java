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
import me.amiralles.materials.control.BomService;
import me.amiralles.materials.entity.Bom;
import me.amiralles.materials.entity.BomItem;
import me.amiralles.materials.entity.BomStatus;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/boms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BomResource {

    @Inject
    BomService service;

    @GET
    public Uni<RestResponse<List<Bom>>> list(
            @QueryParam("productId") Long productId,
            @QueryParam("status") BomStatus status) {
        Uni<List<Bom>> result;
        if (productId != null && status != null) {
            result = service.findByProductAndStatus(productId, status);
        } else {
            result = service.findByProduct(productId);
        }
        return result.map(RestResponse::ok);
    }

    @GET
    @Path("/active")
    public Uni<RestResponse<Bom>> findActive(@QueryParam("productId") Long productId) {
        return service.findActiveByProduct(productId).map(RestResponse::ok);
    }

    @GET
    @Path("/{id}")
    public Uni<RestResponse<Bom>> findById(@PathParam("id") Long id) {
        return service.findById(id).map(RestResponse::ok);
    }

    @GET
    @Path("/{id}/items")
    public Uni<RestResponse<List<BomItem>>> findItems(@PathParam("id") Long id) {
        return service.findItemsByBom(id).map(RestResponse::ok);
    }

    @POST
    public Uni<RestResponse<Bom>> create(@QueryParam("productId") Long productId, Bom bom) {
        return service.create(productId, bom)
                .map(created -> RestResponse.status(RestResponse.Status.CREATED, created));
    }

    @PUT
    @Path("/{id}")
    public Uni<RestResponse<Bom>> update(@PathParam("id") Long id, Bom data) {
        return service.update(id, data).map(RestResponse::ok);
    }

    @DELETE
    @Path("/{id}")
    public Uni<RestResponse<Void>> delete(@PathParam("id") Long id) {
        return service.delete(id).map(ignored -> RestResponse.noContent());
    }
}
