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
import me.amiralles.materials.client.model.BomItem;
import me.amiralles.materials.client.model.BomStatus;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/boms")
@RegisterRestClient(configKey = "bom-client")
@RegisterProvider(RestClientExceptionMapper.class)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface BomClient {

    @GET
    Uni<List<Bom>> list(@QueryParam("productId") Long productId,
                        @QueryParam("status") BomStatus status);

    @GET
    @Path("/active")
    Uni<Bom> findActive(@QueryParam("productId") Long productId);

    @GET
    @Path("/{id}")
    Uni<Bom> findById(@PathParam("id") Long id);

    @GET
    @Path("/{id}/items")
    Uni<List<BomItem>> findItems(@PathParam("id") Long id);

    @POST
    Uni<Bom> create(@QueryParam("productId") Long productId, Bom bom);

    @PUT
    @Path("/{id}")
    Uni<Bom> update(@PathParam("id") Long id, Bom data);

    @DELETE
    @Path("/{id}")
    Uni<Void> delete(@PathParam("id") Long id);
}
