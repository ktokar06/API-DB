package org.example.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.example.config.MyConfig.*;
import static org.example.service.apiPosts.deleteAllPost;
import static org.example.service.apiTags.*;
import static org.example.specification.Specifications.requestSpecification;


public class TagsApiTest{

    /**
     * 2.1
     */
    @Test(description = "Позитивный кейс: Получение списка тегов. Проверка, что созданный тег присутствует в списке.")
    public void listTagTest() {
        Response create = createTag(
                "New Tag for List",
                "This tag is for listing");
        Assert.assertEquals(create.statusCode(), 201, "Ожидался Cтатус-код 201: Created");

        int tagId = create.jsonPath().getInt("id");

        Response list = listTags();
        Assert.assertEquals(list.statusCode(), 200, "Ожидался Статус-код 200: OK");
        Assert.assertTrue(list.jsonPath().getList("id").contains(tagId), "Созданный тег отсутствует в списке");
    }

    @Test(description = "Негативный кейс: Получение списка тегов. Проверка, что список пуст.")
    public void listTagsTest() {
        deleteAllPost();

        Response list = listTags();
        Assert.assertEquals(list.statusCode(), 200, "Ожидался Статус-код 200: OK");
        Assert.assertTrue(list.jsonPath().getList("").isEmpty(), "Список тегов не пуст");
    }

    /**
     * 2.2
     */
    @Test(description = "Позитивный кейс: Создание тега. Проверка, что тег создан с корректными параметрами.")
    public void createTagTest() {
        Response create = createTag(
                "New Tag",
                "This is a description of my new tag");
        Assert.assertEquals(create.statusCode(), 201, "Ожидался Статус-код 201: Created");
        Assert.assertEquals(create.jsonPath().getString("name"), "New Tag", "Имя тега не совпадает");
        Assert.assertEquals(create.jsonPath().getString("description"), "This is a description of my new tag", "Описание тега не совпадает");
    }

    @Test(description = "Негативный кейс: Создание тега без обязательных параметров. Проверка сообщения об ошибке.")
    public void createTagsTest() {
        Response create = RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .body("{\"description\": \"This tag has no name\"}")
                .when()
                .post(URL_TAGS_ENDPOINT)
                .then().log().all()
                .statusCode(400)
                .extract()
                .response();
        Assert.assertEquals(create.statusCode(), 400, "Ожидался Статус-код 400: Bad Request");
        Assert.assertEquals(create.jsonPath().getString("message"), "Missing parameter(s): name", "Сообщение об ошибке не совпадает");
    }

    /**
     * 2.3
     */
    @Test(description = "Позитивный кейс: Получение тега по ID. Проверка, что тег возвращается с корректными параметрами.")
    public void retrieveTagByIDTest() {
        Response create = createTag(
                "New Tag for Retrieve",
                "This tag is for retrieval");

        int tagId = create.jsonPath().getInt("id");

        Response retrieve = retrieveTagById(tagId);
        Assert.assertEquals(retrieve.statusCode(), 200, "Ожидался Статус-код 200: OK");
        Assert.assertEquals(retrieve.jsonPath().getString("name"), "New Tag for Retrieve", "Имя тега не совпадает");
        Assert.assertEquals(retrieve.jsonPath().getString("description"), "This tag is for retrieval", "Описание тега не совпадает");
    }

    @Test(description = "Негативный кейс: Получение несуществующего тега по ID. Проверка сообщения об ошибке.")
    public void retrieveTagsByIDTest() {
        Response retrieve = RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", 999)
                .when()
                .get(URL_TAGS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(404)
                .extract()
                .response();
        Assert.assertEquals(retrieve.statusCode(), 404, "Ожидался Статус-код 404: Not Found");
        Assert.assertEquals(retrieve.jsonPath().getString("message"), "Term does not exist.", "Сообщение об ошибке не совпадает");
    }

    /**
     * 2.4
     */
    @Test(description = "Позитивный кейс: Обновление тега. Проверка, что тег обновлен с новыми параметрами.")
    public void updateTagsTest() {
        Response create = createTag(
                "New Tag for Update",
                "This tag is for updating");

        int tagId = create.jsonPath().getInt("id");

        Response update = updateTag(tagId, "Updated Tag Name", "Updated description");
        Assert.assertEquals(update.statusCode(), 200, "Ожидался Статус-код 200: OK");
        Assert.assertEquals(update.jsonPath().getString("name"), "Updated Tag Name", "Имя тега не обновлено");
        Assert.assertEquals(update.jsonPath().getString("description"), "Updated description", "Описание тега не обновлено");
    }

    @Test(description = "Негативный кейс: Обновление несуществующего тега. Проверка сообщения об ошибке.")
    public void updateTagTest() {
        Response update = RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", 999)
                .body("{\"name\": \"Updated Tag Name\"," +
                        " \"description\": \"Updated description\"}")
                .when()
                .put(URL_TAGS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(404)
                .extract()
                .response();
        Assert.assertEquals(update.statusCode(), 404, "Ожидался Статус-код 404: Not Found");
        Assert.assertEquals(update.jsonPath().getString("message"), "Term does not exist.", "Сообщение об ошибке не совпадает");
    }

    /**
     * 2.5
     */
    @Test(description = "Позитивный кейс: Удаление тега. Проверка, что тег удален.")
    public void deleteTagTest() {
        Response create = createTag(
                "New Tag for Deletion",
                "This tag will be deleted");

        int tagId = create.jsonPath().getInt("id");

        Response delete = deleteTag(tagId);
        Assert.assertEquals(delete.statusCode(), 200, "Ожидался Статус-код 200: OK");

        Response tagsId = tagById(tagId);
        Assert.assertEquals(tagsId.statusCode(), 404, "Tag не был удален");
    }

    @Test(description = "Негативный кейс: Удаление несуществующего тега. Проверка сообщения об ошибке.")
    public void deleteTagsTest() {
        Response delete = RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", 999)
                .when()
                .delete(URL_TAGS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(404)
                .extract()
                .response();
        Assert.assertEquals(delete.statusCode(), 404, "Ожидался Статус-код 404: Not Found");
        Assert.assertEquals(delete.jsonPath().getString("message"), "Term does not exist.", "Сообщение об ошибке не совпадает");
    }

    /**
     * Чистка данных
     * */
    @AfterMethod
    public void cleanupData() {
        deleteAllTag();
    }
}