package org.example.test;

import org.example.service.dbPosts;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.example.service.dbPosts.*;

public class PostsDbTest extends BaseTest {

    /**
     * 1.1
     */
    @Test(description = "Позитивный кейс: Тестовый случай Получение списка постов.")
    public void listPostTest() throws SQLException {
        int postId = dbPosts.createPost("Test Title", "Test Content", 1, "publish");

        ResultSet rs = getPostById(postId);
        Assert.assertTrue(rs.next(), "Пост не найден");
        Assert.assertEquals(rs.getString("post_title"), "Test Title", "Заголовок поста не совпадает");
        Assert.assertEquals(rs.getString("post_content"), "Test Content", "Содержание поста не совпадает");
    }

    @Test(description = "Негативный кейс: Тестовый случай сообщение Записей нет")
    public void listPostsTest() throws SQLException {
        deleteAllPosts();

        ResultSet rs = getPostById(999);
        Assert.assertFalse(rs.next(), "Пост не должен быть найден");
    }

    /**
     * 1.2
     */
    @Test(description = "Позитивный кейс: Создание поста. Проверка, что пост создан с корректными параметрами.")
    public void createPostTest() throws SQLException {
        int postId = createPost("Test Title", "Test Content", 1, "publish");

        ResultSet rs = getPostById(postId);
        Assert.assertTrue(rs.next(), "Пост не найден");
        Assert.assertEquals(rs.getString("post_title"), "Test Title", "Заголовок поста не совпадает");
        Assert.assertEquals(rs.getString("post_content"), "Test Content", "Содержание поста не совпадает");
        Assert.assertEquals(rs.getInt("post_author"), 1, "Автор поста не совпадает");
        Assert.assertEquals(rs.getString("post_status"), "publish", "Статус поста не совпадает");
    }

    /**
     * 1.3
     */
    @Test(description = "Позитивный кейс: Получение поста по ID. Проверка, что пост возвращается с корректными параметрами.")
    public void retrievePostByIDTest() throws SQLException {
        int postId = createPost("Test Title", "Test Content", 1, "publish");

        ResultSet rs = getPostById(postId);
        Assert.assertTrue(rs.next(), "Пост не найден");
        Assert.assertEquals(rs.getString("post_title"), "Test Title", "Заголовок поста не совпадает");
        Assert.assertEquals(rs.getString("post_content"), "Test Content", "Содержание поста не совпадает");
    }

    @Test(description = "Негативный кейс: Получение несуществующего поста по ID. Проверка сообщения об ошибке.")
    public void retrievePostsByIDTest() throws SQLException {
        ResultSet rs = getPostById(999);
        Assert.assertFalse(rs.next(), "Пост не найден");
    }

    /**
     * 1.4
     */
    @Test(description = "Позитивный кейс: Обновление поста. Проверка, что пост обновлен с новыми параметрами.")
    public void updatePostTest() throws SQLException {
        int postId = createPost("Test Title", "Test Content", 1, "publish");

        int updatedRows = updatePost(postId, "Updated Title", "Updated Content");
        Assert.assertEquals(updatedRows, 1, "Пост не был обновлен");

        ResultSet rs = getPostById(postId);
        Assert.assertTrue(rs.next(), "Пост не найден");
        Assert.assertEquals(rs.getString("post_title"), "Updated Title", "Заголовок поста не совпадает");
        Assert.assertEquals(rs.getString("post_content"), "Updated Content", "Содержание поста не совпадает");
    }

    @Test(description = "Негативный кейс: Обновление несуществующего поста. Проверка сообщения об ошибке.")
    public void updatePostsTest() throws SQLException {
        int updatedRows = updatePost(999, "Updated Title", "Updated Content");
        Assert.assertEquals(updatedRows, 0, "Пост не должен быть обновлен");
    }

    /**
     * 1.5
     */
    @Test(description = "Позитивный кейс: Удаление поста. Проверка, что пост удален.")
    public void deletePostTest() throws SQLException {
        int postId = createPost("Test Title", "Test Content", 1, "publish");

        int deletedRows = deletePost(postId);
        Assert.assertEquals(deletedRows, 1, "Пост не был удален");

        ResultSet rs = getPostById(postId);
        Assert.assertFalse(rs.next(), "Пост не найден");
    }

    @Test(description = "Негативный кейс: Удаление несуществующего поста. Проверка сообщения об ошибке.")
    public void deletePostsTest() throws SQLException {
        int deletedRows = deletePost(999);
        Assert.assertEquals(deletedRows, 0, "Пост не найден");
    }
}