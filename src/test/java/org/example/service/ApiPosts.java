package org.example.service;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.example.config.MyConfig.*;
import static org.example.specification.Specifications.requestSpecification;

public class ApiPosts {

    // cоздание post
    public static Response createPost(String title, String content, int categoryId, String status) {
        String body = String.format("{\"title\":\"%s\",\"content\":\"%s\",\"categories\":[%d],\"status\":\"%s\"}",
                title, content, categoryId, status);
        return RestAssured.given()
                .spec(requestSpecification())
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .body(body)
                .when()
                .post(URL_POSTS_ENDPOINT)
                .then().log().all()
                .statusCode(201)
                .extract()
                .response();
    }

    // получение списка post
    public static Response listPosts() {
        return RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .when()
                .get(URL_POSTS_ENDPOINT)
                .then().log().all()
                .statusCode(200)
                .extract()
                .response();

    }

    // получение post по ID
    public static Response retrievePostById(int postId) {
        return RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", postId)
                .when()
                .get(URL_POSTS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(200)
                .extract()
                .response();
    }

    // обновление post
    public static Response updatePost(int postId, String title, String content) {
        String body = String.format("{\"title\": \"%s\", \"content\": \"%s\"}", title, content);
        return RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", postId)
                .body(body)
                .when()
                .put(URL_POSTS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(200)
                .extract()
                .response();
    }

    // удаление post
    public static Response deletePost(int postId) {
        return RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", postId)
                .queryParam("force", true)
                .when()
                .delete(URL_POSTS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(200)
                .extract()
                .response();
    }

    // удаление post полностью
    public static Response deleteAllPost() {
        // список всех post
        Response response = listPosts();

        if (response.statusCode() == 200) {
            List<Integer> postIds = response.jsonPath().getList("id");

            if (!postIds.isEmpty()) {
                // Удаляем post
                for (Integer postId : postIds) {
                    deletePost(postId);
                }
                System.out.println("Все Post успешно удалены.");
            } else {
                System.out.println("Нет Post для удаления.");
            }
        } else {
            System.out.println("Не удалось получить Post, статус: " + response.statusCode());
        }
        return response;
    }

    // информации по id
    public static Response postById(int postId) {
        return given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", postId)
                .when()
                .get("/posts/{id}")
                .then()
                .extract().response();
    }
}
