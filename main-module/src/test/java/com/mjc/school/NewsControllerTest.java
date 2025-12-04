package com.mjc.school;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Main.class)
@ActiveProfiles("test")
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NewsControllerTest {

    @LocalServerPort
    private int port;

    private static final String BASE_PATH = "/stage3-module4-task/v1/news";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = BASE_PATH;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void getById_withValidId_shouldReturnAuthor() {
        given()
                .pathParam("id", 1L)
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", notNullValue())
                .body("_links.self.href", endsWith("/news/1"));
    }


    @Test
    void getById_withInvalidId_shouldReturn404() {
        given()
                .pathParam("id", 99999L)
                .when()
                .get("/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    void create_withBlankName_shouldReturn400() {
        String json = """
            {
                "title": "",
                "content": "Some content",
                "authorId": 1,
                "tagIds": []
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    void update_withMismatchedId_shouldReturn400() {
        String json = """
            {
                "id": 999,
                "title": "Wrong ID News",
                "content": "Content here",
                "authorId": 1,
                "tagIds": []
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1L)
                .body(json)
                .when()
                .put("/{id}")
                .then()
                .statusCode(400);
    }

    @Test
    void deleteById_withInvalidId_shouldReturn404() {
        given()
                .pathParam("id", 99999L)
                .when()
                .delete("/{id}")
                .then()
                .statusCode(404);
    }
}