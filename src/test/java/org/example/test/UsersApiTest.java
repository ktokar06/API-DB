package org.example.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import java.util.List;

import static org.example.config.MyConfig.*;
import static org.example.service.apiUsers.*;
import static org.example.specification.Specifications.requestSpecification;


public class UsersApiTest{

    /**
     * 3.1
     */
    @Test(description = "Позитивный кейс: Проверка наличия системного администратора.")
    public void checkAdminExistsTest() {
        Response listResponse = listUsers();
        Assert.assertEquals(listResponse.statusCode(), 200, "Ожидался статус 200");

        List<Integer> userIds = listResponse.jsonPath().getList("id");
        Assert.assertTrue(userIds.contains(1), "Администратор не найден");

        Response adminResponse = retrieveUserById(1);
        Assert.assertEquals(adminResponse.jsonPath().getString("name"), USERNAME, "Логин администратора не совпадает");
    }

    /**
     * 3.2
     */
    @Test(description = "Позитивный кейс: Создание пользователя. Проверка, что пользователь создан с корректными параметрами.")
    public void createUserTest() {
        Response create = createUser(
                "Joe_Beta",
                "joe_beta@example.com",
                "strong_password");
        Assert.assertEquals(create.statusCode(), 201, "Статус-код 201: Created");
        Assert.assertEquals(create.jsonPath().getString("username"), "Joe_Beta", "Имя пользователя не совпадает");
        Assert.assertEquals(create.jsonPath().getString("email"), "joe_beta@example.com", "Email пользователя не совпадает");
    }

    @Test(description = "Негативный кейс: Создание пользователя без обязательных параметров. Проверка сообщения об ошибке.")
    public void createUsersTest() {
        Response create = RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .body("{\"username\": \"Joe_Invalid\", \"password\": \"strong_password\"}")
                .when()
                .post(URL_USERS_ENDPOINT)
                .then().log().all()
                .statusCode(400)
                .extract()
                .response();
        Assert.assertEquals(create.statusCode(), 400, "Статус-код 400: Bad Request");
        Assert.assertEquals(create.jsonPath().getString("message"), "Missing parameter(s): email", "Сообщение об ошибке не совпадает");
    }

    /**
     * 3.3
     */
    @Test(description = "Позитивный кейс: Получение пользователя по ID. Проверка, что пользователь возвращается с корректными параметрами.")
    public void retrieveUserByIDTest() {
        Response create = createUser(
                "Joe_Gamma",
                "joe_gamma@example.com",
                "strong_password");

        int userId = create.jsonPath().getInt("id");

        Response retrieve = retrieveUserById(userId);
        Assert.assertEquals(retrieve.statusCode(), 200, "Статус-код 200: OK");
        Assert.assertEquals(retrieve.jsonPath().getString("name"), "Joe_Gamma", "Имя пользователя не совпадает");
    }

    @Test(description = "Негативный кейс: Получение несуществующего пользователя по ID. Проверка сообщения об ошибке.")
    public void retrieveUsersByIdTest() {
        Response retrieve = RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", 999)
                .when()
                .get(URL_USERS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(404)
                .extract()
                .response();
        Assert.assertEquals(retrieve.statusCode(), 404, "Статус-код 404: Not Found");
        Assert.assertEquals(retrieve.jsonPath().getString("message"), "Invalid user ID.", "Сообщение об ошибке не совпадает");
    }

    /**
     * 3.4
     */
    @Test(description = "Позитивный кейс: Обновление пользователя. Проверка, что пользователь обновлен с новыми параметрами.")
    public void updateUserTest() {
        Response create = createUser(
                "Joe_Delta",
                "joe_delta@example.com",
                "strong_password");

        int userId = create.jsonPath().getInt("id");

        Response update = updateUser(
                userId,
                "joe_updated@example.com",
                "John",
                "Doe");
        Assert.assertEquals(update.statusCode(), 200, "Статус-код 200: OK");
        Assert.assertEquals(update.jsonPath().getString("email"), "joe_updated@example.com", "Email пользователя не обновлен");
        Assert.assertEquals(update.jsonPath().getString("first_name"), "John", "Имя пользователя не обновлено");
        Assert.assertEquals(update.jsonPath().getString("last_name"), "Doe", "Фамилия пользователя не обновлена");
    }

    @Test(description = "Негативный кейс: Обновление несуществующего пользователя. Проверка сообщения об ошибке.")
    public void updateUsersTest() {
        Response update = RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", 999)
                .body("{\"email\": \"joe_invalid@example.com\"}")
                .when()
                .put(URL_USERS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(404)
                .extract()
                .response();
        Assert.assertEquals(update.statusCode(), 404, "Статус-код 404: Not Found");
        Assert.assertEquals(update.jsonPath().getString("message"), "Invalid user ID.", "Сообщение об ошибке не совпадает");
    }

    /**
     * 3.5
     */
    @Test(description = "Позитивный кейс: Удаление пользователя. Проверка, что пользователь удален.")
    public void deleteUserTest() {
        Response create = createUser(
                "Joe_Epsilon",
                "joe_epsilon@example.com",
                "strong_password");

        int userId = create.jsonPath().getInt("id");

        Response delete = deleteUser(userId);
        Assert.assertEquals(delete.statusCode(), 200, "Ожидался Статус-код 200: OK");

        Response getDeletedPost = userById(userId);
        Assert.assertEquals(getDeletedPost.statusCode(), 404, "Пользователь не был удален");
    }

    @Test(description = "Негативный кейс: Удаление несуществующего пользователя. Проверка сообщения об ошибке.")
    public void deleteUsersTest() {
        Response delete = RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", 999)
                .queryParam("reassign", false)
                .queryParam("force", true)
                .when()
                .delete(URL_USERS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(404)
                .extract()
                .response();
        Assert.assertEquals(delete.statusCode(), 404, "Статус-код 404: Not Found");
        Assert.assertEquals(delete.jsonPath().getString("message"), "Invalid user ID.", "Сообщение об ошибке не совпадает");
    }

    /**
     * 3.6
     */
    @Test(description = "Позитивный кейс: Получение информации о текущем пользователе.")
    public void retrieveUserMeTest() {
        Response retrieve = retrieveUserById(1);

        Assert.assertEquals(retrieve.statusCode(), 200, "Статус-код 200: OK");
        Assert.assertEquals(retrieve.jsonPath().getString("name"), USERNAME, "Логин текущего пользователя не совпадает");
    }

    @Test(description = "Негативный кейс: Получение информации о текущем пользователе без авторизации. Проверка сообщения об ошибке.")
    public void retrieveUsersMeTest() {
        Response retrieve = RestAssured.given()
                .spec(requestSpecification())
                .when()
                .get(URL_USERS_ENDPOINT + "/me")
                .then().log().all()
                .statusCode(401)
                .extract()
                .response();
        Assert.assertEquals(retrieve.statusCode(), 401, "Статус-код 401: Unauthorized");
        Assert.assertEquals(retrieve.jsonPath().getString("message"), "You are not currently logged in.", "Сообщение об ошибке не совпадает");
    }

    /**
     * 3.7
     */
    @Test(description = "Позитивный кейс: Обновление информации о текущем пользователе.")
    public void updateUserMeTest() {
        Response update = updateUser(
                1,
                "joe_me@example.com",
                "John",
                "Doe");

        Assert.assertEquals(update.statusCode(), 200, "Статус-код 200: OK");
        Assert.assertEquals(update.jsonPath().getString("email"), "joe_me@example.com", "Email пользователя не обновлен");
        Assert.assertEquals(update.jsonPath().getString("first_name"), "John", "Имя пользователя не обновлено");
        Assert.assertEquals(update.jsonPath().getString("last_name"), "Doe", "Фамилия пользователя не обновлена");
    }

    @Test(description = "Негативный кейс: Обновление информации о текущем пользователе с невалидными параметрами. Проверка сообщения об ошибке.")
    public void updateUsersMeTest() {
        Response update = RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", 1)
                .body("{\"email\": \"invalid_email\"}")
                .when()
                .put(URL_USERS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(400)
                .extract()
                .response();
        Assert.assertEquals(update.statusCode(), 400, "Статус-код 400: Bad Request");
        Assert.assertEquals(update.jsonPath().getString("data.details.email.message"), "Invalid email address.", "Сообщение об ошибке не совпадает");
    }

    /**
    * Чистка данных
    * */
    @AfterMethod
    public void cleanupData() {
        Response listResponse = listUsers();
        List<Integer> userIds = listResponse.jsonPath().getList("id");
        for (Integer userId : userIds) {
            if (userId != 1) {
                deleteUser(userId);
            }
        }
    }
}