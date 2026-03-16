package me.amiralles.materials.boundary;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class BomResourceTest {

    private static final String BOMS_PATH = "/boms";
    private static final String PRODUCTS_PATH = "/products";

    private static final String BASELINE_PRODUCT_PAYLOAD = """
            {
              "sku": "BOM-TEST-PROD-001",
              "name": "BOM Test Product",
              "category": "SHIRT",
              "status": "ACTIVE"
            }
            """;

    private static final String BASELINE_BOM_PAYLOAD = """
            {
              "version": 1,
              "status": "DRAFT",
              "effectiveDate": "2026-01-01"
            }
            """;

    private Long baselineId;
    private Long baselineProductId;
    private boolean alreadyDeleted;

    @BeforeEach
    void setUp() {
        alreadyDeleted = false;

        baselineProductId = given()
                .contentType("application/json")
                .body(BASELINE_PRODUCT_PAYLOAD)
                .when()
                .post(PRODUCTS_PATH)
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");

        baselineId = given()
                .contentType("application/json")
                .queryParam("productId", baselineProductId)
                .body(BASELINE_BOM_PAYLOAD)
                .when()
                .post(BOMS_PATH)
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
                    .delete(BOMS_PATH + "/" + baselineId)
                    .then()
                    .statusCode(204);
        }
        if (baselineProductId != null) {
            given()
                    .when()
                    .delete(PRODUCTS_PATH + "/" + baselineProductId)
                    .then()
                    .statusCode(204);
        }
    }

    // --- GET /boms?productId=X ---

    @Test
    void listByProduct_returnsBaseline() {
        given()
                .queryParam("productId", baselineProductId)
                .when()
                .get(BOMS_PATH)
                .then()
                .statusCode(200)
                .body("id", hasItem(baselineId.intValue()));
    }

    @Test
    void listByProductAndStatus_returnsBaseline() {
        given()
                .queryParam("productId", baselineProductId)
                .queryParam("status", "DRAFT")
                .when()
                .get(BOMS_PATH)
                .then()
                .statusCode(200)
                .body("id", hasItem(baselineId.intValue()));
    }

    @Test
    void listByProductAndStatus_returnsEmptyForOtherStatus() {
        given()
                .queryParam("productId", baselineProductId)
                .queryParam("status", "ACTIVE")
                .when()
                .get(BOMS_PATH)
                .then()
                .statusCode(200)
                .body("$", empty());
    }

    // --- GET /boms/active?productId=X ---

    @Test
    void findActive_returns404_whenNoneActive() {
        given()
                .queryParam("productId", baselineProductId)
                .when()
                .get(BOMS_PATH + "/active")
                .then()
                .statusCode(404);
    }

    // --- GET /boms/{id} ---

    @Test
    void findById_returnsBom() {
        given()
                .when()
                .get(BOMS_PATH + "/" + baselineId)
                .then()
                .statusCode(200)
                .body("id", equalTo(baselineId.intValue()));
    }

    @Test
    void findById_returns404_whenNotFound() {
        given()
                .when()
                .get(BOMS_PATH + "/999999")
                .then()
                .statusCode(404);
    }

    // --- GET /boms/{id}/items ---

    @Test
    void findItems_returns200WithEmptyList() {
        given()
                .when()
                .get(BOMS_PATH + "/" + baselineId + "/items")
                .then()
                .statusCode(200)
                .body("$", empty());
    }

    // --- POST /boms ---

    @Test
    void create_returns201WithBody() {
        String payload = """
                {
                  "version": 2,
                  "status": "DRAFT",
                  "effectiveDate": "2026-06-01"
                }
                """;

        Long newId = given()
                .contentType("application/json")
                .queryParam("productId", baselineProductId)
                .body(payload)
                .when()
                .post(BOMS_PATH)
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("version", equalTo(2))
                .extract()
                .jsonPath()
                .getLong("id");

        // cleanup
        given().when().delete(BOMS_PATH + "/" + newId).then().statusCode(204);
    }

    // --- PUT /boms/{id} ---

    @Test
    void update_returnsUpdatedBom() {
        String updatePayload = """
                {
                  "version": 1,
                  "status": "ACTIVE",
                  "effectiveDate": "2026-01-01"
                }
                """;

        given()
                .contentType("application/json")
                .body(updatePayload)
                .when()
                .put(BOMS_PATH + "/" + baselineId)
                .then()
                .statusCode(200)
                .body("status", equalTo("ACTIVE"));
    }

    @Test
    void update_returns404_whenNotFound() {
        String updatePayload = """
                {
                  "version": 1,
                  "status": "ACTIVE",
                  "effectiveDate": "2026-01-01"
                }
                """;

        given()
                .contentType("application/json")
                .body(updatePayload)
                .when()
                .put(BOMS_PATH + "/999999")
                .then()
                .statusCode(404);
    }

    // --- DELETE /boms/{id} ---

    @Test
    void delete_returns204() {
        given()
                .when()
                .delete(BOMS_PATH + "/" + baselineId)
                .then()
                .statusCode(204);
        alreadyDeleted = true;
    }

    @Test
    void delete_returns404_whenNotFound() {
        given()
                .when()
                .delete(BOMS_PATH + "/999999")
                .then()
                .statusCode(404);
    }
}
