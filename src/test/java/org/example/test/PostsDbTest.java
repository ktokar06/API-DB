package org.example.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.service.dbPosts.*;
import static org.testng.Assert.*;

public class PostsDbTest extends BaseTest {

    private final String updatedTitle = "Updated Title";
    private final String updatedContent = "Updated Content";

    private final String post_title = "post_title";
    private final String post_content = "post_content";

    /**
     * 1.1
     */
    @Test(description = "Позитивный кейс: Тестовый случай Получение списка постов.")
    public void listPostTest() throws SQLException {
        int postId = createPost("Test Title", "Test Content", 1, "publish");

        ResultSet resultSet = getPostById(postId);
        Assert.assertTrue(resultSet.next(), "Пост не найден");
        assertEquals(resultSet.getString(post_title), "Test Title", "Заголовок поста не совпадает");
        assertEquals(resultSet.getString(post_content), "Test Content", "Содержание поста не совпадает");
    }

    @Test(description = "Негативный кейс: Тестовый случай сообщение Записей нет")
    public void listPostsNoPostsReturnsEmptyTest() throws SQLException {
        int postCount = getPostCount();
        assertEquals(postCount, 0, "Ожидалось, что список постов будет пустым");
    }

    /**
     * 1.2
     */
    @Test(description = "Позитивный кейс: Создание поста. Проверка, что пост создан с корректными параметрами.")
    public void createPostTest() throws SQLException {
        int postId = createPost("Test Title", "Test Content", 1, "publish");

        ResultSet resultSet = getPostById(postId);
        Assert.assertTrue(resultSet.next(), "Пост не найден");
        assertEquals(resultSet.getString(post_title), "Test Title", "Заголовок поста не совпадает");
        assertEquals(resultSet.getString(post_content), "Test Content", "Содержание поста не совпадает");
        assertEquals(resultSet.getInt("post_author"), 1, "Автор поста не совпадает");
        assertEquals(resultSet.getString("post_status"), "publish", "Статус поста не совпадает");
    }

    /**
     * 1.3
     */
    @Test(description = "Позитивный кейс: Получение поста по ID. Проверка, что пост возвращается с корректными параметрами.")
    public void retrievePostByIDTest() throws SQLException {
        int postId = createPost("Test Title", "Test Content", 1, "publish");

        ResultSet resultSet = getPostById(postId);
        Assert.assertTrue(resultSet.next(), "Пост не найден");
        assertEquals(resultSet.getString(post_title), "Test Title", "Заголовок поста не совпадает");
        assertEquals(resultSet.getString(post_content), "Test Content", "Содержание поста не совпадает");
    }

    @Test(description = "Негативный кейс: Получение несуществующего поста по ID. Проверка сообщения об ошибке.")
    public void retrieveNonexistentPostByIdTest() throws SQLException {
        ResultSet resultSet = getPostById(999);
        Assert.assertFalse(resultSet.next(), "Ожидалось отсутствие поста для несуществующего ID");
        Assert.assertFalse(resultSet.isBeforeFirst(), "Результат запроса должен быть пустым");
    }

    /**
     * 1.4
     */
    @Test(description = "Позитивный кейс: Обновление поста. Проверка, что пост обновлен с новыми параметрами.")
    public void updatePostTest() throws SQLException {
        int postId = createPost("Test Title", "Test Content", 1, "publish");

        int updatedRows = updatePost(postId, updatedTitle, updatedContent);
        assertEquals(updatedRows, 1, "Пост не был обновлен");

        ResultSet resultSet = getPostById(postId);
        Assert.assertTrue(resultSet.next(), "Пост не найден");
        assertEquals(resultSet.getString(post_title), updatedTitle, "Заголовок поста не совпадает");
        assertEquals(resultSet.getString(post_content), updatedContent, "Содержание поста не совпадает");
    }

    @Test(description = "Негативный кейс: Обновление несуществующего поста. Проверка сообщения об ошибке.")
    public void updatePostsTest() throws SQLException {
        int updatedRows = updatePost(999, updatedTitle, updatedContent);
        assertEquals(updatedRows, 0, "Пост не должен быть обновлен");
    }


    /**
     * 1.5
     */
    @Test(description = "Позитивный кейс: Удаление поста. Проверка, что пост удален.")
    public void deletePostTest() throws SQLException {
        int postId = createPost("Test Title", "Test Content", 1, "publish");

        int deletedRows = deletePost(postId);
        assertEquals(deletedRows, 1, "Пост не был удален");

        ResultSet resultSet = getPostById(postId);
        Assert.assertFalse(resultSet.next(), "Пост не найден");
    }

    @Test(description = "Негативный кейс: Удаление несуществующего поста. Проверка сообщения об ошибке.")
    public void deleteNonexistentPostTest() throws SQLException {
        int deletedRows = deletePost(999);
        assertEquals(deletedRows, 0, "Пост не найден");
    }
}