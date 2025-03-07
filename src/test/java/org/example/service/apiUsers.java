package org.example.service;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.example.config.MyConfig.*;
import static org.example.specification.Specifications.requestSpecification;


public class apiUsers {

    // создание user
    public static Response createUser (String username, String email, String password) {
        String body = String.format("{\"username\": \"%s\", \"email\": \"%s\", \"password\": \"%s\"}", username, email, password);
        return given()
                .spec(requestSpecification())
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .body(body)
                .when()
                .post(URL_USERS_ENDPOINT)
                .then().log().all()
                .statusCode(201)
                .extract()
                .response();
    }

    // получение списка user
    public static Response listUsers() {
        return given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .when()
                .get(URL_USERS_ENDPOINT)
                .then().log().all()
                .statusCode(200)
                .extract()
                .response();
    }

    // получение user по ID
    public static Response retrieveUserById(int userId) {
        return given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", userId)
                .when()
                .get(URL_USERS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(200)
                .extract()
                .response();
    }

    // обновление user
    public static Response updateUser (int userId, String email, String firstName, String lastName) {
        String body = String.format("{\"email\": \"%s\", \"first_name\": \"%s\", \"last_name\": \"%s\"}", email, firstName, lastName);
        return given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", userId)
                .body(body)
                .when()
                .put(URL_USERS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(200)
                .extract()
                .response();
    }

    // удаление user
    public static Response deleteUser (int userId) {
        return given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", userId)
                .queryParam("reassign", false)
                .queryParam("force", true)
                .when()
                .delete(URL_USERS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(200)
                .extract()
                .response();
    }

    // информации по id
    public static Response userById(int userId) {
        return given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", userId)
                .when()
                .get("/users/{id}")
                .then()
                .extract().response();
    }
}