package me.amiralles.materials.client;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import me.amiralles.materials.client.model.Product;
import me.amiralles.materials.client.model.ProductCategory;
import me.amiralles.materials.client.model.ProductStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class ProductClientTest {

    @Inject
    @RestClient
    ProductClient client;

    @Test
    void list_returnsAllProducts() {
        var result = client.list(null, null, null).await().indefinitely();
        assertThat(result).isNotNull();
    }

    @Test
    void list_filteredByStatus_returnsActiveProducts() {
        var result = client.list(null, ProductStatus.ACTIVE, null).await().indefinitely();
        assertThat(result).allSatisfy(p -> assertThat(p.status).isEqualTo(ProductStatus.ACTIVE));
    }

    @Test
    void create_thenFindById_thenDelete() {
        var product = new Product();
        product.sku = "TST-PROD-001";
        product.name = "Test Shirt";
        product.category = ProductCategory.SHIRT;
        product.status = ProductStatus.DEVELOPMENT;

        var created = client.create(product).await().indefinitely();
        assertThat(created.id).isNotNull();

        var found = client.findById(created.id).await().indefinitely();
        assertThat(found.sku).isEqualTo("TST-PROD-001");

        client.delete(created.id).await().indefinitely();
    }
}
