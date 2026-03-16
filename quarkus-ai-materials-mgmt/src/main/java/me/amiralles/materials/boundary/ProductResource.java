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
import me.amiralles.materials.control.ProductService;
import me.amiralles.materials.entity.Bom;
import me.amiralles.materials.entity.BomStatus;
import me.amiralles.materials.entity.Material;
import me.amiralles.materials.entity.Product;
import me.amiralles.materials.entity.ProductCategory;
import me.amiralles.materials.entity.ProductStatus;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService service;

    @Inject
    BomService bomService;

    @GET
    public Uni<RestResponse<List<Product>>> list(
            @QueryParam("sku") String sku,
            @QueryParam("status") ProductStatus status,
            @QueryParam("category") ProductCategory category) {
        Uni<List<Product>> result;
        if (sku != null) {
            result = service.findBySku(sku).map(List::of);
        } else if (status != null && category != null) {
            result = service.findByStatusAndCategory(status, category);
        } else if (status != null) {
            result = service.findByStatus(status);
        } else if (category != null) {
            result = service.findByCategory(category);
        } else {
            result = service.listAll();
        }
        return result.map(RestResponse::ok);
    }

    @GET
    @Path("/{id}")
    public Uni<RestResponse<Product>> findById(@PathParam("id") Long id) {
        return service.findById(id).map(RestResponse::ok);
    }

    @GET
    @Path("/{id}/materials")
    public Uni<RestResponse<List<Material>>> findMaterials(@PathParam("id") Long id) {
        return service.findMaterialsForProduct(id).map(RestResponse::ok);
    }

    @GET
    @Path("/{id}/boms")
    public Uni<RestResponse<List<Bom>>> listBoms(
            @PathParam("id") Long id,
            @QueryParam("status") BomStatus status) {
        return service.findById(id)
                .flatMap(p -> status != null
                        ? bomService.findByProductAndStatus(id, status)
                        : bomService.findByProduct(id))
                .map(RestResponse::ok);
    }

    @GET
    @Path("/{id}/boms/active")
    public Uni<RestResponse<Bom>> getActiveBom(@PathParam("id") Long id) {
        return service.findById(id)
                .flatMap(p -> bomService.findActiveByProduct(id))
                .map(RestResponse::ok);
    }

    @POST
    public Uni<RestResponse<Product>> create(Product product) {
        return service.create(product)
                .map(created -> RestResponse.status(RestResponse.Status.CREATED, created));
    }

    @PUT
    @Path("/{id}")
    public Uni<RestResponse<Product>> update(@PathParam("id") Long id, Product data) {
        return service.update(id, data).map(RestResponse::ok);
    }

    @DELETE
    @Path("/{id}")
    public Uni<RestResponse<Void>> delete(@PathParam("id") Long id) {
        return service.delete(id).map(ignored -> RestResponse.noContent());
    }
}
