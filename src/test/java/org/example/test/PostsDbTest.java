package org.example.test;

import org.example.service.dbPosts;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.config.MyConfig.*;
import static org.example.service.dbPosts.*;


public class PostsDbTest extends BaseTest {

    /**
     * 1.1
     */
    @Test(description = "Позитивный кейс: Тестовый случай Получение списка постов.")
    public void listPostsTest() throws SQLException {
        int postId = createPost(TEST_TITLE, TEST_CONTENT, AUTHOR_ID, POST_STATUS_PUBLISH);

        ResultSet rs = getPostById(postId);
        Assert.assertTrue(rs.next(), "Пост не найден");
        Assert.assertEquals(rs.getString(POST_TITLE_FIELD), TEST_TITLE, "Заголовок поста не совпадает");
        Assert.assertEquals(rs.getString(POST_CONTENT_FIELD), TEST_CONTENT, "Содержание поста не совпадает");
    }

    @Test(description = "Негативный кейс: Тестовый случай сообщение Записей нет")
    public void listPostsNoPostsReturnsEmptyTest() throws SQLException {
        int postCount = dbPosts.getPostCount();
        Assert.assertEquals(postCount, 0, "Количество постов должно быть 0");
    }

    /**
     * 1.2
     */
    @Test(description = "Позитивный кейс: Создание поста. Проверка, что пост создан с корректными параметрами.")
    public void createPostWithValidParamsTest() throws SQLException {
        int postId = createPost(TEST_TITLE, TEST_CONTENT, AUTHOR_ID, POST_STATUS_PUBLISH);

        ResultSet resultSet = getPostById(postId);
        Assert.assertTrue(resultSet.next(), "Пост не найден");
        Assert.assertEquals(resultSet.getString(POST_TITLE_FIELD), TEST_TITLE, "Заголовок поста не совпадает");
        Assert.assertEquals(resultSet.getString(POST_CONTENT_FIELD), TEST_CONTENT, "Содержание поста не совпадает");
        Assert.assertEquals(resultSet.getInt("post_author"), AUTHOR_ID, "Автор поста не совпадает");
        Assert.assertEquals(resultSet.getString("post_status"), POST_STATUS_PUBLISH, "Статус поста не совпадает");
    }

    /**
     * 1.3
     */
    @Test(description = "Позитивный кейс: Получение поста по ID. Проверка, что пост возвращается с корректными параметрами.")
    public void retrievePostByIdTest() throws SQLException {
        int postId = createPost(TEST_TITLE, TEST_CONTENT, AUTHOR_ID, POST_STATUS_PUBLISH);

        ResultSet resultSet = getPostById(postId);
        Assert.assertTrue(resultSet.next(), "Пост не найден");
        Assert.assertEquals(resultSet.getString(POST_TITLE_FIELD), TEST_TITLE, "Заголовок поста не совпадает");
        Assert.assertEquals(resultSet.getString(POST_CONTENT_FIELD), TEST_CONTENT, "Содержание поста не совпадает");
    }

    @Test(description = "Негативный кейс: Получение несуществующего поста по ID. Проверка сообщения об ошибке.")
    public void retrieveNonexistentPostByIdTest() throws SQLException {
        ResultSet resultSet = getPostById(NO_EXISTENT_POST_ID);
        Assert.assertFalse(resultSet.next(), "Пост не найден");
        Assert.assertFalse(resultSet.isBeforeFirst(), "Результат запроса должен быть пустым");
    }

    /**
     * 1.4
     */
    @Test(description = "Позитивный кейс: Обновление поста. Проверка, что пост обновлен с новыми параметрами.")
    public void updatePostWithValidParamsTest() throws SQLException {
        int postId = createPost(TEST_TITLE, TEST_CONTENT, AUTHOR_ID, POST_STATUS_PUBLISH);

        int updatedRows = updatePost(postId, UPDATED_TITLE, UPDATED_CONTENT);
        Assert.assertEquals(updatedRows, 1, "Пост не был обновлен");

        ResultSet resultSet = getPostById(postId);
        Assert.assertTrue(resultSet.next(), "Пост не найден");
        Assert.assertEquals(resultSet.getString(POST_TITLE_FIELD), UPDATED_TITLE, "Заголовок поста не совпадает");
        Assert.assertEquals(resultSet.getString(POST_CONTENT_FIELD), UPDATED_CONTENT, "Содержание поста не совпадает");
    }

    @Test(description = "Негативный кейс: Обновление несуществующего поста. Проверка сообщения об ошибке.")
    public void updateNonexistentPostTest() throws SQLException {
        int updatedRows = updatePost(NO_EXISTENT_POST_ID, UPDATED_TITLE, UPDATED_CONTENT);
        Assert.assertEquals(updatedRows, 0, "Пост не должен быть обновлен");
    }

    /**
     * 1.5
     */
    @Test(description = "Позитивный кейс: Удаление поста. Проверка, что пост удален.")
    public void deletePostTest() throws SQLException {
        int postId = createPost(TEST_TITLE, TEST_CONTENT, AUTHOR_ID, POST_STATUS_PUBLISH);

        int deletedRows = deletePost(postId);
        Assert.assertEquals(deletedRows, 1, "Пост не был удален");

        ResultSet resultSet = getPostById(postId);
        Assert.assertFalse(resultSet.next(), "Пост не найден");
    }

    @Test(description = "Негативный кейс: Удаление несуществующего поста. Проверка сообщения об ошибке.")
    public void deleteNonexistentPostTest() throws SQLException {
        int deletedRows = deletePost(NO_EXISTENT_POST_ID);
        Assert.assertEquals(deletedRows, 0, "Пост не найден");
    }
}