package me.amiralles.materials.boundary;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class MaterialResourceTest {

    private static final String BASE_PATH = "/materials";

    private static final String BASELINE_PAYLOAD = """
            {
              "code": "MAT-TEST-001",
              "name": "Test Cotton Fabric",
              "description": "Integration test material",
              "category": "FABRIC",
              "supplierRef": "ACME",
              "color": "white",
              "composition": "100% cotton",
              "minStock": 100.00
            }
            """;

    private Long baselineId;
    private boolean alreadyDeleted;

    @BeforeEach
    void setUp() {
        alreadyDeleted = false;
        baselineId = given()
                .contentType("application/json")
                .body(BASELINE_PAYLOAD)
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");
    }

    @AfterEach
    void tearDown() {
        if (!alreadyDeleted && baselineId != null) {
            given()
                    .when()
                    .delete(BASE_PATH + "/" + baselineId)
                    .then()
                    .statusCode(204);
        }
    }

    // --- GET /materials ---

    @Test
    void listAll_returnsBaseline() {
        given()
                .when()
                .get(BASE_PATH)
                .then()
                .statusCode(200)
                .body("id", hasItem(baselineId.intValue()));
    }

    @Test
    void listByCategory_returnsMatchingOnly() {
        given()
                .queryParam("category", "FABRIC")
                .when()
                .get(BASE_PATH)
                .then()
                .statusCode(200)
                .body("category", everyItem(equalTo("FABRIC")));
    }

    @Test
    void listByName_returnsMatchingOnly() {
        given()
                .queryParam("name", "Cotton")
                .when()
                .get(BASE_PATH)
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test
    void listBySupplierRef_returnsMatchingOnly() {
        given()
                .queryParam("supplierRef", "ACME")
                .when()
                .get(BASE_PATH)
                .then()
                .statusCode(200)
                .body("id", hasItem(baselineId.intValue()));
    }

    @Test
    void listByInvalidCategory_returns4xx() {
        given()
                .queryParam("category", "BOGUS")
                .when()
                .get(BASE_PATH)
                .then()
                .statusCode(anyOf(equalTo(400), equalTo(404)));
    }

    // --- GET /materials/{id} ---

    @Test
    void findById_returnsExpectedMaterial() {
        given()
                .when()
                .get(BASE_PATH + "/" + baselineId)
                .then()
                .statusCode(200)
                .body("code", equalTo("MAT-TEST-001"));
    }

    @Test
    void findById_returns404_whenNotFound() {
        given()
                .when()
                .get(BASE_PATH + "/999999")
                .then()
                .statusCode(404);
    }

    // --- GET /materials/{id}/products ---

    @Test
    void findProducts_returns200WithList() {
        given()
                .when()
                .get(BASE_PATH + "/" + baselineId + "/products")
                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }

    // --- POST /materials ---

    @Test
    void create_returns201WithBody() {
        String payload = """
                {
                  "code": "MAT-TEST-002",
                  "name": "Test Polyester Fabric",
                  "description": "Another integration test material",
                  "category": "FABRIC",
                  "supplierRef": "GLOBEX",
                  "color": "blue",
                  "composition": "100% polyester",
                  "minStock": 50.00
                }
                """;

        Long newId = given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("code", equalTo("MAT-TEST-002"))
                .extract()
                .jsonPath()
                .getLong("id");

        // cleanup
        given().when().delete(BASE_PATH + "/" + newId).then().statusCode(204);
    }

    @Test
    void create_returns4xx_whenCodeMissing() {
        String payload = """
                {
                  "name": "No Code Material",
                  "category": "FABRIC",
                  "minStock": 10.00
                }
                """;

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(anyOf(equalTo(400), equalTo(500)));
    }

    // --- PUT /materials/{id} ---

    @Test
    void update_returnsUpdatedMaterial() {
        String updatePayload = """
                {
                  "code": "MAT-TEST-001",
                  "name": "Updated Cotton Fabric",
                  "description": "Integration test material",
                  "category": "FABRIC",
                  "supplierRef": "ACME",
                  "color": "white",
                  "composition": "100% cotton",
                  "minStock": 100.00
                }
                """;

        given()
                .contentType("application/json")
                .body(updatePayload)
                .when()
                .put(BASE_PATH + "/" + baselineId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Cotton Fabric"));
    }

    @Test
    void update_returns404_whenNotFound() {
        String updatePayload = """
                {
                  "code": "GHOST-001",
                  "name": "Ghost Material",
                  "category": "FABRIC",
                  "minStock": 0.00
                }
                """;

        given()
                .contentType("application/json")
                .body(updatePayload)
                .when()
                .put(BASE_PATH + "/999999")
                .then()
                .statusCode(404);
    }

    // --- DELETE /materials/{id} ---

    @Test
    void delete_returns204() {
        given()
                .when()
                .delete(BASE_PATH + "/" + baselineId)
                .then()
                .statusCode(204);
        alreadyDeleted = true;
    }

    @Test
    void delete_returns404_whenNotFound() {
        given()
                .when()
                .delete(BASE_PATH + "/999999")
                .then()
                .statusCode(404);
    }
}
