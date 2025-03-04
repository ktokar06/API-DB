package org.example.service;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.example.config.MyConfig.*;
import static org.example.specification.Specifications.requestSpecification;

public class Tags {

    // cоздание tag
    public static Response createTag(String name, String description) {
        String body = String.format("{\"name\": \"%s\", \"description\": \"%s\"}", name, description);
        return RestAssured.given()
                .spec(requestSpecification())
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .body(body)
                .when()
                .post(URL_TAGS_ENDPOINT)
                .then().log().all()
                .statusCode(201)
                .extract()
                .response();
    }

    // получение списка tag
    public static Response listTags() {
        return RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .when()
                .get(URL_TAGS_ENDPOINT)
                .then().log().all()
                .statusCode(200)
                .extract()
                .response();
    }

    // получение tag по ID
    public static Response retrieveTagById(int tagId) {
        return RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", tagId)
                .when()
                .get(URL_TAGS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(200)
                .extract()
                .response();
    }

    // обновление tag
    public static Response updateTag(int tagId, String name, String description) {
        String body = String.format("{\"name\": \"%s\", \"description\": \"%s\"}", name, description);
        return RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", tagId)
                .body(body)
                .when()
                .put(URL_TAGS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(200)
                .extract()
                .response();
    }

    // удаление tag
    public static Response deleteTag(int tagId) {
        return RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", tagId)
                .queryParam("force", true)
                .when()
                .delete(URL_TAGS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(200)
                .extract()
                .response();
    }

    // удаление tag полностью
    public static Response deleteAllTag() {
        // список всех tag
        Response response = listTags();

        if (response.statusCode() == 200) {
            List<Integer> tagIds = response.jsonPath().getList("id");

            if (!tagIds.isEmpty()) {
                // Удаляем tag
                for (Integer tagId : tagIds) {
                    deleteTag(tagId);
                }
                System.out.println("Все Tag успешно удалены.");
            } else {
                System.out.println("Нет Tag для удаления.");
            }
        } else {
            System.out.println("Не удалось получить Tag, статус: " + response.statusCode());
        }
        return response;
    }

    // информации по id
    public static Response tagById(int tagId) {
        return given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", tagId)
                .when()
                .get("/tags/{id}")
                .then()
                .extract().response();
    }
}

