package me.amiralles.materials.boundary;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class MaterialAttachmentResourceTest {

    private Long materialId;

    @BeforeEach
    void setUp() {
        materialId = given()
            .contentType("application/json")
            .body("""
                {"code":"ATT-001","name":"Test Fabric","category":"FABRIC","minStock":5.00}
                """)
            .when().post("/materials")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");
    }

    @AfterEach
    void tearDown() {
        if (materialId != null)
            given().when().delete("/materials/" + materialId).then().statusCode(204);
    }

    @Test
    void listAttachments_returnsEmptyInitially() {
        given().when().get("/materials/" + materialId + "/attachments")
            .then().statusCode(200).body("$", empty());
    }

    @Test
    void addAndListAttachment_roundtrip() {
        Long id = given().contentType("application/json")
            .body("""
                {"url":"https://example.com/swatch.jpg","attachmentType":"IMAGE",
                 "fileName":"swatch.jpg","contentType":"image/jpeg"}
                """)
            .when().post("/materials/" + materialId + "/attachments")
            .then().statusCode(201)
            .body("id", notNullValue())
            .body("url", equalTo("https://example.com/swatch.jpg"))
            .body("attachmentType", equalTo("IMAGE"))
            .extract().jsonPath().getLong("id");

        given().when().get("/materials/" + materialId + "/attachments")
            .then().statusCode(200).body("$.size()", equalTo(1));

        given().when().delete("/materials/" + materialId + "/attachments/" + id)
            .then().statusCode(204);
    }

    @Test
    void getAttachment_returns200() {
        Long id = given().contentType("application/json")
            .body("""
                {"url":"https://example.com/spec.pdf","attachmentType":"DOCUMENT"}
                """)
            .when().post("/materials/" + materialId + "/attachments")
            .then().statusCode(201).extract().jsonPath().getLong("id");

        given().when().get("/materials/" + materialId + "/attachments/" + id)
            .then().statusCode(200)
            .body("url", equalTo("https://example.com/spec.pdf"));

        given().when().delete("/materials/" + materialId + "/attachments/" + id)
            .then().statusCode(204);
    }

    @Test
    void getAttachment_returns404_whenNotFound() {
        given().when().get("/materials/" + materialId + "/attachments/999999")
            .then().statusCode(404);
    }

}
