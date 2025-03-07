package org.example.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.example.config.MyConfig.*;
import static org.example.service.apiPosts.*;
import static org.example.specification.Specifications.requestSpecification;


public class PostsApiTest{

    /**
     * 1.1
     */
    @Test(description = "Позитивный кейс: Тестовый случай Получение списка постов.")
    public void listPostTest() {
        // создаем post
        Response create = createPost(
                "New Post one",
                "This is the content of my new post",
                1, "publish");
        Assert.assertEquals(create.statusCode(), 201, "Ожидался Cтатус-код 201: Created");

        // ID созданного поста
        int postId = create.jsonPath().getInt("id");

        // получаем список post
        Response list = listPosts();
        Assert.assertEquals(list.statusCode(), 200, "Ожидался Статус-код 200: OK");

        // проверяем наличие созданного поста в списке по ID
        List<Integer> postIds = list.jsonPath().getList("id");
        Assert.assertTrue(postIds.contains(postId), "Созданный пост отсутствует в списке");

        // ищем список заголовков
        List<String> title = list.jsonPath().getList("title.rendered");
        Assert.assertFalse(list.jsonPath().getList("").isEmpty(), "Список постов пуст");
        Assert.assertTrue(title.contains("New Post one"), "Пост с заголовком 'New Post one' не найден");
    }

    @Test(description = "Негативный кейс: Тестовый случай сообщение Записей нет")
    public void listPostsTest() {
        // удаляем все post перед тестом
        deleteAllPost();

        // получаем список post
        Response list = listPosts();
        Assert.assertEquals(list.statusCode(), 200, "Ожидался Статус-код 200: OK");
        Assert.assertTrue(list.jsonPath().getList("").isEmpty(), "Список постов не пуст");
    }

    /**
     * 1.2
     */
    @Test(description = "Позитивный кейс: Создание поста. Проверка, что пост создан с корректными параметрами.")
    public void createPostTest() {
        // создаем post
        Response create = createPost(
                "New Post",
                "This is the content of my new post",
                1, "publish");
        Assert.assertEquals(create.statusCode(), 201, "Ожидался Cтатус-код 201: Created");
        Assert.assertEquals(create.jsonPath().getString("title.raw"), "New Post", "Заголовок поста не совпадает");
        Assert.assertEquals(create.jsonPath().getString("content.raw"), "This is the content of my new post", "Содержание поста не совпадает");
    }

    @Test(description = "Негативный кейс: Создание поста без обязательных параметров. Проверка сообщения об ошибке.")
    public void createPostsTest() {
        // создаем post без параметров
        Response create = RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .body("{}")
                .when()
                .post(URL_POSTS_ENDPOINT)
                .then().log().all()
                .statusCode(400)
                .extract()
                .response();
        Assert.assertEquals(create.statusCode(), 400, "Ожидался Статус-код 400: Bad Request");
        Assert.assertTrue(create.jsonPath().getString("message").contains("Content, title, and excerpt are empty"), "Сообщение об ошибке не совпадает");
    }

    /**
     * 1.3
     */
    @Test(description = "Позитивный кейс: Получение поста по ID. Проверка, что пост возвращается с корректными параметрами.")
    public void retrievePostByIDTest() {
        // создаем post
        Response create = createPost(
                "New Post",
                "This is the content of my new post",
                1, "publish");

        // ID созданного поста
        int postId = create.jsonPath().getInt("id");

        // получаем post по ID
        Response retrieve = retrievePostById(postId);
        Assert.assertEquals(retrieve.statusCode(), 200, "Ожидался Статус-код 200: OK");
        Assert.assertEquals(retrieve.jsonPath().getString("title.rendered"), "New Post", "Заголовок поста не совпадает");
        Assert.assertEquals(retrieve.jsonPath().getInt("author"), 1, "ID автора не совпадает");
        Assert.assertEquals(retrieve.jsonPath().getString("status"), "publish", "Статус поста не совпадает");
    }

    @Test(description = "Негативный кейс: Получение несуществующего поста по ID. Проверка сообщения об ошибке.")
    public void retrievePostsByIDTest() {
        // получаем несуществующий post
        Response retrieve = RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", 999)
                .when()
                .get(URL_POSTS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(404)
                .extract()
                .response();
        Assert.assertEquals(retrieve.statusCode(), 404, "Ожидался Статус-код 404: Not Found");
        Assert.assertEquals(retrieve.jsonPath().getString("message"), "Invalid post ID.", "Сообщение об ошибке не совпадает");
    }

    /**
     * 1.4
     */
    @Test(description = "Позитивный кейс: Обновление поста. Проверка, что пост обновлен с новыми параметрами.")
    public void updatePostTest() {
        // создаем post
        Response create = createPost(
                "New Post",
                "This is the content of my new post",
                1, "publish");

        // ID созданного поста
        int postId = create.jsonPath().getInt("id");

        // обновляем post
        Response update = updatePost(postId, "Updated Title", "Updated content");
        Assert.assertEquals(update.statusCode(), 200, "Ожидался Статус-код 200: OK");
        Assert.assertEquals(update.jsonPath().getString("title.raw"), "Updated Title", "Заголовок поста не обновлен");
        Assert.assertEquals(update.jsonPath().getString("content.raw"), "Updated content", "Содержание поста не обновлено");
    }

    @Test(description = "Негативный кейс: Обновление несуществующего поста. Проверка сообщения об ошибке.")
    public void updatePostsTest() {
        // обновляем несуществующий post
        Response update = RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", 999)
                .body("{\"title\": \"Updated Title\", \"content\": \"Updated content\"}")
                .when()
                .put(URL_POSTS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(404)
                .extract()
                .response();
        Assert.assertEquals(update.statusCode(), 404, "Ожидался Статус-код 404: Not Found");
        Assert.assertEquals(update.jsonPath().getString("message"), "Invalid post ID.", "Сообщение об ошибке не совпадает");
    }

    /**
     * 1.5
     */
    @Test(description = "Позитивный кейс: Удаление поста. Проверка, что пост удален.")
    public void deletePostTest() {
        // создаем пост
        Response create = createPost(
                "New Post for Deletion",
                "This post will be deleted.",
                1, "publish");

        int postId = create.jsonPath().getInt("id");

        // удаляем пост
        Response delete = deletePost(postId);
        Assert.assertEquals(delete.statusCode(), 200, "Ожидался Статус-код 200: OK");

        //проверяем что поста больше нет
        Response postIds = postById(postId);
        Assert.assertEquals(postIds.statusCode(), 404, "Пост не был удален");
    }

    @Test(description = "Негативный кейс: Удаление несуществующего поста. Проверка сообщения об ошибке.")
    public void deletePostsTest() {
        // пытаемся удалить несуществующий post
        Response delete = RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", 999)
                .when()
                .delete(URL_POSTS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(404)
                .extract()
                .response();
        Assert.assertEquals(delete.statusCode(), 404, "Статус-код 404: Not Found");
        Assert.assertEquals(delete.jsonPath().getString("message"), "Invalid post ID.", "Сообщение об ошибке не совпадает");
    }

    @Test(description = "Негативный кейс: Повторное удаление уже удаленного поста. Проверка сообщения об ошибке.")
    public void deletePostsExistingTest() {
        // создаем пост
        Response create = createPost(
                "New Post for Deletion",
                "This post will be deleted.",
                1, "publish");

        // ID созданного поста
        int postId = create.jsonPath().getInt("id");

        // удаляем пост
        Response delete = deletePost(postId);
        Assert.assertEquals(delete.statusCode(), 200, "Ожидался Статус-код 200: OK");

        // удалить пост повторно
        Response reDelete = RestAssured.given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .spec(requestSpecification())
                .pathParam("id", postId)
                .queryParam("force", true)
                .when()
                .delete(URL_POSTS_ENDPOINT + "/{id}")
                .then().log().all()
                .statusCode(404)
                .extract()
                .response();
        Assert.assertEquals(reDelete.jsonPath().getString("message"), "Invalid post ID.", "Сообщение об ошибке не совпадает");
    }

    /**
     * Чистка данных
     * */
    @AfterMethod
    public void cleanupData() {
        deleteAllPost();
    }
}