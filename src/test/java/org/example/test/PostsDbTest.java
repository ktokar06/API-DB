package org.example.test;

import org.example.service.dbPosts;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostsDbTest extends BaseTest {

    /**
     * 1.1
     */
    @Test(description = "Позитивный кейс: Тестовый случай Получение списка постов.")
    public void listPostTest() throws SQLException {
        int postId = dbPosts.createPost("Test Title", "Test Content", 1, "publish");

        ResultSet rs = dbPosts.getPostById(postId);
        Assert.assertTrue(rs.next(), "Пост не найден");
        Assert.assertTrue(rs.getString("post_title").equals("Test Title"), "Заголовок поста не совпадает");
        Assert.assertTrue(rs.getString("post_content").equals("Test Content"), "Содержание поста не совпадает");
    }

    @Test(description = "Негативный кейс: Тестовый случай сообщение Записей нет")
    public void listPostsTest() throws SQLException {
        dbPosts.deleteAllPosts();

        ResultSet rs = dbPosts.getPostById(999);
        Assert.assertFalse(rs.next(), "Пост не должен быть найден");
    }

    /**
     * 1.2
     */
    @Test(description = "Позитивный кейс: Создание поста. Проверка, что пост создан с корректными параметрами.")
    public void createPostTest() throws SQLException {
        int postId = dbPosts.createPost("Test Title", "Test Content", 1, "publish");

        ResultSet rs = dbPosts.getPostById(postId);
        Assert.assertTrue(rs.next(), "Пост не найден");
        Assert.assertTrue(rs.getString("post_title").equals("Test Title"), "Заголовок поста не совпадает");
        Assert.assertTrue(rs.getString("post_content").equals("Test Content"), "Содержание поста не совпадает");
        Assert.assertTrue(rs.getInt("post_author") == 1, "Автор поста не совпадает");
        Assert.assertTrue(rs.getString("post_status").equals("publish"), "Статус поста не совпадает");
    }

    /**
     * 1.3
     */
    @Test(description = "Позитивный кейс: Получение поста по ID. Проверка, что пост возвращается с корректными параметрами.")
    public void retrievePostByIDTest() throws SQLException {
        int postId = dbPosts.createPost("Test Title", "Test Content", 1, "publish");

        ResultSet rs = dbPosts.getPostById(postId);
        Assert.assertTrue(rs.next(), "Пост не найден");
        Assert.assertTrue(rs.getString("post_title").equals("Test Title"), "Заголовок поста не совпадает");
        Assert.assertTrue(rs.getString("post_content").equals("Test Content"), "Содержание поста не совпадает");
    }

    @Test(description = "Негативный кейс: Получение несуществующего поста по ID. Проверка сообщения об ошибке.")
    public void retrievePostsByIDTest() throws SQLException {
        ResultSet rs = dbPosts.getPostById(999);
        Assert.assertFalse(rs.next(), "Пост не найден");
    }

    /**
     * 1.4
     */
    @Test(description = "Позитивный кейс: Обновление поста. Проверка, что пост обновлен с новыми параметрами.")
    public void updatePostTest() throws SQLException {
        int postId = dbPosts.createPost("Test Title", "Test Content", 1, "publish");

        int updatedRows = dbPosts.updatePost(postId, "Updated Title", "Updated Content");
        Assert.assertTrue(updatedRows == 1, "Пост не был обновлен");

        ResultSet rs = dbPosts.getPostById(postId);
        Assert.assertTrue(rs.next(), "Пост не найден");
        Assert.assertTrue(rs.getString("post_title").equals("Updated Title"), "Заголовок поста не совпадает");
        Assert.assertTrue(rs.getString("post_content").equals("Updated Content"), "Содержание поста не совпадает");
    }

    @Test(description = "Негативный кейс: Обновление несуществующего поста. Проверка сообщения об ошибке.")
    public void updatePostsTest() throws SQLException {
        int updatedRows = dbPosts.updatePost(999, "Updated Title", "Updated Content");
        Assert.assertTrue(updatedRows == 0, "Пост не должен быть обновлен");
    }

    /**
     * 1.5
     */
    @Test(description = "Позитивный кейс: Удаление поста. Проверка, что пост удален.")
    public void deletePostTest() throws SQLException {
        int postId = dbPosts.createPost("Test Title", "Test Content", 1, "publish");

        int deletedRows = dbPosts.deletePost(postId);
        Assert.assertTrue(deletedRows == 1, "Пост не был удален");

        ResultSet rs = dbPosts.getPostById(postId);
        Assert.assertFalse(rs.next(), "Пост не найден");
    }

    @Test(description = "Негативный кейс: Удаление несуществующего поста. Проверка сообщения об ошибке.")
    public void deletePostsTest() throws SQLException {
        int deletedRows = dbPosts.deletePost(999);
        Assert.assertTrue(deletedRows == 0, "Пост не найден");
    }
}