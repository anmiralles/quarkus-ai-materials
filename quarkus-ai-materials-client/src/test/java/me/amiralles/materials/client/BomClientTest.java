package me.amiralles.materials.client;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import me.amiralles.materials.client.model.Bom;
import me.amiralles.materials.client.model.BomStatus;
import me.amiralles.materials.client.model.Product;
import me.amiralles.materials.client.model.ProductCategory;
import me.amiralles.materials.client.model.ProductStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class BomClientTest {

    @Inject
    @RestClient
    BomClient bomClient;

    @Inject
    @RestClient
    ProductClient productClient;

    private Long productId;

    @BeforeEach
    void createProduct() {
        var product = new Product();
        product.sku = "BOM-TEST-PROD-001";
        product.name = "Bom Test Product";
        product.category = ProductCategory.JACKET;
        product.status = ProductStatus.DEVELOPMENT;
        productId = productClient.create(product).await().indefinitely().id;
    }

    @AfterEach
    void deleteProduct() {
        productClient.delete(productId).await().indefinitely();
    }

    @Test
    void list_returnsAllBoms() {
        var result = bomClient.list(null, null).await().indefinitely();
        assertThat(result).isNotNull();
    }

    @Test
    void list_filteredByProduct_returnsBoms() {
        var result = bomClient.list(productId, null).await().indefinitely();
        assertThat(result).isNotNull();
    }

    @Test
    void create_thenFindById_thenDelete() {
        var bom = new Bom();
        bom.version = 1;
        bom.status = BomStatus.DRAFT;
        bom.effectiveDate = LocalDate.now();

        var created = bomClient.create(productId, bom).await().indefinitely();
        assertThat(created.id).isNotNull();

        var found = bomClient.findById(created.id).await().indefinitely();
        assertThat(found.version).isEqualTo(1);

        bomClient.delete(created.id).await().indefinitely();
    }
}
