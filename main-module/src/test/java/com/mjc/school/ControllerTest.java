package com.mjc.school;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ControllerTest {

    @LocalServerPort
    private int port;

    private static Long createdAuthorId;
    private static Long createdNewsId;
    private static Long createdTagId;
    private static Long createdCommentId;

    @BeforeEach
    void setUp(){
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/stage3-module4-task/v1";
    }

    @Test
    @Order(1)
    void testCreateAuthor(){
        String requestBody= """
                {"name":"REST Assured Author"}
                """;

        createdAuthorId=given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/authors")
                .then()
                .statusCode(201)
                .body("name",equalTo("REST Assured Author"))
                .body("_links.self.href", containsString("/authors/"))
                .extract()
                .path("id");

        Assertions.assertNotNull(createdAuthorId);
    }

    @Test
    @Order(2)
    void testGetAllAuthors() {
        given()
                .when()
                .get("/authors")
                .then()
                .statusCode(200)
                .body("_embedded.authorDtoResponseList.size()", greaterThan(0))
                .body("_links.self.href", not(emptyString()));
    }

    @Test
    @Order(3)
    void testGetAuthorById() {
        given()
                .pathParam("id", createdAuthorId)
                .when()
                .get("/authors/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(createdAuthorId.intValue()))
                .body("name", equalTo("REST Assured Author"));
    }

    @Test
    @Order(4)
    void testCreateTag() {
        String requestBody = """
            { "name": "spring-boot" }
            """;

        createdTagId = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/tags")
                .then()
                .statusCode(201)
                .body("name", equalTo("spring-boot"))
                .extract()
                .path("id");
    }

    @Test
    @Order(5)
    void testCreateNewsWithAuthorAndTags() {
        String requestBody = """
            {
                "title": "REST Assured Test News",
                "content": "This news was created by integration test",
                "authorId": %d,
                "tagIds": [%d]
            }
            """.formatted(createdAuthorId, createdTagId);

        createdNewsId = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/news")
                .then()
                .statusCode(201)
                .body("title", equalTo("REST Assured Test News"))
                .body("authorDto.name", equalTo("REST Assured Author"))
                .body("tagsDto.name", hasItem("spring-boot"))
                .extract()
                .path("id");

        Assertions.assertNotNull(createdNewsId);
    }

    @Test
    @Order(6)
    void testGetNewsById() {
        given()
                .pathParam("id", createdNewsId)
                .when()
                .get("/news/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(createdNewsId.intValue()))
                .body("title", equalTo("REST Assured Test News"));
    }

    @Test
    @Order(7)
    void testSearchNewsByParams() {
        given()
                .queryParam("title", "REST Assured Test News")
                .when()
                .get("/news")
                .then()
                .statusCode(200)
                .body("_embedded.newsDtoResponseList[0].title", containsString("REST Assured"));
    }

    @Test
    @Order(8)
    void testCreateComment() {
        String requestBody = """
            {
                "content": "Great article!",
                "newsId": %d
            }
            """.formatted(createdNewsId);

        createdCommentId = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/comments")
                .then()
                .statusCode(201)
                .body("content", equalTo("Great article!"))
                .extract()
                .path("id");
    }

    @Test
    @Order(9)
    void testGetCommentsByNewsId() {
        given()
                .pathParam("newsId", createdNewsId)
                .when()
                .get("/comments/by-news/{newsId}")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("content", hasItem("Great article!"));
    }

    @Test
    @Order(10)
    void testGetTagsByNewsId() {
        given()
                .pathParam("newsId", createdNewsId)
                .when()
                .get("/tags/by-news/{newsId}")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("name", hasItem("spring-boot"));
    }

    @Test
    @Order(11)
    void testUpdateAuthor() {
        String requestBody = """
            {
                "id": %d,
                "name": "Updated REST Author"
            }
            """.formatted(createdAuthorId);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .pathParam("id", createdAuthorId)
                .when()
                .put("/authors/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated REST Author"));
    }

    @Test
    @Order(12)
    void testPatchNews() {
        String patchBody = """
            { "title": "PATCHED: REST Assured News" }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(patchBody)
                .pathParam("id", createdNewsId)
                .when()
                .patch("/news/{id}")
                .then()
                .statusCode(200)
                .body("title", equalTo("PATCHED: REST Assured News"));
    }

    @Test
    @Order(13)
    void testDeleteComment() {
        given()
                .pathParam("id", createdCommentId)
                .when()
                .delete("/comments/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(14)
    void testDeleteNews() {
        given()
                .pathParam("id", createdNewsId)
                .when()
                .delete("/news/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(15)
    void testDeleteAuthor() {
        given()
                .pathParam("id", createdAuthorId)
                .when()
                .delete("/authors/{id}")
                .then()
                .statusCode(204);
    }
}
