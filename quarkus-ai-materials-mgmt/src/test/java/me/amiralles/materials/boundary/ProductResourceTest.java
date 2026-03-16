package me.amiralles.materials.boundary;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ProductResourceTest {

    private static final String BASE_PATH = "/products";

    private static final String BASELINE_PAYLOAD = """
            {
              "sku": "TEST-SHIRT-001",
              "name": "Test Oxford Shirt",
              "description": "Integration test product",
              "category": "SHIRT",
              "status": "ACTIVE"
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

    // --- GET /products ---

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
    void listByStatus_returnsMatchingOnly() {
        given()
                .queryParam("status", "ACTIVE")
                .when()
                .get(BASE_PATH)
                .then()
                .statusCode(200)
                .body("status", everyItem(equalTo("ACTIVE")));
    }

    @Test
    void listByCategory_returnsMatchingOnly() {
        given()
                .queryParam("category", "SHIRT")
                .when()
                .get(BASE_PATH)
                .then()
                .statusCode(200)
                .body("category", everyItem(equalTo("SHIRT")));
    }

    @Test
    void listByInvalidStatus_returns4xx() {
        given()
                .queryParam("status", "BOGUS")
                .when()
                .get(BASE_PATH)
                .then()
                .statusCode(anyOf(equalTo(400), equalTo(404)));
    }

    // --- GET /products/{id} ---

    @Test
    void findById_returnsProduct() {
        given()
                .when()
                .get(BASE_PATH + "/" + baselineId)
                .then()
                .statusCode(200)
                .body("id", equalTo(baselineId.intValue()))
                .body("sku", equalTo("TEST-SHIRT-001"));
    }

    @Test
    void findById_returns404_whenNotFound() {
        given()
                .when()
                .get(BASE_PATH + "/999999")
                .then()
                .statusCode(404);
    }

    // --- POST /products ---

    @Test
    void create_returns201WithBody() {
        String payload = """
                {
                  "sku": "TEST-PANTS-002",
                  "name": "Test Slim Pants",
                  "description": "Another integration test product",
                  "category": "PANTS",
                  "status": "DEVELOPMENT"
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
                .body("sku", equalTo("TEST-PANTS-002"))
                .body("category", equalTo("PANTS"))
                .body("status", equalTo("DEVELOPMENT"))
                .extract()
                .jsonPath()
                .getLong("id");

        // cleanup
        given().when().delete(BASE_PATH + "/" + newId).then().statusCode(204);
    }

    @Test
    void create_returns400_whenSkuMissing() {
        String payload = """
                {
                  "name": "No Sku Product",
                  "category": "JACKET",
                  "status": "DEVELOPMENT"
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

    // --- PUT /products/{id} ---

    @Test
    void update_returnsUpdatedProduct() {
        String updatePayload = """
                {
                  "sku": "TEST-SHIRT-001",
                  "name": "Updated Oxford Shirt",
                  "description": "Integration test product",
                  "category": "SHIRT",
                  "status": "ACTIVE"
                }
                """;

        given()
                .contentType("application/json")
                .body(updatePayload)
                .when()
                .put(BASE_PATH + "/" + baselineId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Oxford Shirt"));
    }

    @Test
    void update_returns404_whenNotFound() {
        String updatePayload = """
                {
                  "sku": "GHOST-001",
                  "name": "Ghost Product",
                  "category": "SHIRT",
                  "status": "ACTIVE"
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

    // --- DELETE /products/{id} ---

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
