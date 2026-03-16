package me.amiralles.materials.client;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import me.amiralles.materials.client.model.Material;
import me.amiralles.materials.client.model.MaterialCategory;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class MaterialClientTest {

    @Inject
    @RestClient
    MaterialClient client;

    @Test
    void list_returnsAllMaterials() {
        var result = client.list(null, null, null).await().indefinitely();
        assertThat(result).isNotNull();
    }

    @Test
    void list_filteredByCategory_returnsFabricMaterials() {
        var result = client.list(null, null, MaterialCategory.FABRIC).await().indefinitely();
        assertThat(result).allSatisfy(m -> assertThat(m.category).isEqualTo(MaterialCategory.FABRIC));
    }

    @Test
    void create_thenFindById_thenDelete() {
        var mat = new Material();
        mat.code = "TEST-001";
        mat.name = "Test Cotton";
        mat.category = MaterialCategory.FABRIC;

        var created = client.create(mat).await().indefinitely();
        assertThat(created.id).isNotNull();

        var found = client.findById(created.id).await().indefinitely();
        assertThat(found.code).isEqualTo("TEST-001");

        client.delete(created.id).await().indefinitely();
    }
}
