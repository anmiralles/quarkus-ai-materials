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
import me.amiralles.materials.client.model.BomItem;
import me.amiralles.materials.client.model.BomStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Path("/boms")
@Produces(MediaType.APPLICATION_JSON)
public class BomResource {

    @Inject
    @RestClient
    BomClient bomClient;

    @GET
    public Uni<List<Bom>> list(@QueryParam("productId") Long productId,
                               @QueryParam("status") BomStatus status) {
        return bomClient.list(productId, status);
    }

    @GET
    @Path("/active")
    public Uni<Bom> findActive(@QueryParam("productId") Long productId) {
        return bomClient.findActive(productId);
    }

    @GET
    @Path("/{id}")
    public Uni<Bom> findById(@PathParam("id") Long id) {
        return bomClient.findById(id);
    }

    @GET
    @Path("/{id}/items")
    public Uni<List<BomItem>> findItems(@PathParam("id") Long id) {
        return bomClient.findItems(id);
    }
}
