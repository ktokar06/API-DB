package org.example.test;

import org.example.service.dbTags;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TagsDbTest extends BaseTest {

    /**
     * 2.1
     */
    @Test(description = "Позитивный кейс: Получение списка тегов. Проверка, что созданный тег присутствует в списке.")
    public void listTagTest() throws SQLException {
        int tagId = dbTags.createTag("Test Tag");
        ResultSet tags = dbTags.getTagById(tagId);
        Assert.assertTrue(tags.next(), "Тег не найден в списке");
    }

    @Test(description = "Негативный кейс: Получение списка тегов. Проверка, что список пуст.")
    public void listTagsTest() throws SQLException {
        ResultSet tags = dbTags.getTagById(999);
        Assert.assertFalse(tags.next(), "Список тегов должен быть пустым");
    }

    /**
     * 2.2
     */
    @Test(description = "Позитивный кейс: Создание тега. Проверка, что тег создан с корректными параметрами.")
    public void createTagTest() throws SQLException {
        String tagName = "Test Tag";
        int tagId = dbTags.createTag(tagName);

        ResultSet tag = dbTags.getTagById(tagId);
        Assert.assertTrue(tag.next(), "Тег не найден");
        Assert.assertTrue(tag.getString("name").equals(tagName), "Название тега не совпадает");
        Assert.assertTrue(tag.getString("slug").equals("test-tag"), "Тег не совпадает");
    }

    /**
     * 2.3
     */
    @Test(description = "Позитивный кейс: Получение тега по ID. Проверка, что тег возвращается с корректными параметрами.")
    public void retrieveTagByIDTest() throws SQLException {
        int tagId = dbTags.createTag("Test Tag");
        ResultSet tag = dbTags.getTagById(tagId);
        Assert.assertTrue(tag.next(), "Тег не найден");
        Assert.assertTrue(tag.getString("name").equals("Test Tag"), "Название тега не совпадает");
    }

    @Test(description = "Негативный кейс: Получение несуществующего тега по ID. Проверка сообщения об ошибке.")
    public void retrieveTagsByIDTest() throws SQLException {
        ResultSet tag = dbTags.getTagById(999);
        Assert.assertFalse(tag.next(), "Тег не должен существовать");
    }

    /**
     * 2.4
     */
    @Test(description = "Позитивный кейс: Обновление тега. Проверка, что тег обновлен с новыми параметрами.")
    public void updateTagsTest() throws SQLException {
        int tagId = dbTags.createTag("Old Name");
        int updatedRows = dbTags.updateTag(tagId, "New Name");

        Assert.assertTrue(updatedRows == 1, "Тег не обновлен");
        ResultSet tag = dbTags.getTagById(tagId);
        Assert.assertTrue(tag.next());
        Assert.assertTrue(tag.getString("name").equals("New Name"), "Название не обновлено");
        Assert.assertTrue(tag.getString("slug").equals("new-name"), "Тег не обновлен");
    }

    @Test(description = "Негативный кейс: Обновление несуществующего тега. Проверка сообщения об ошибке.")
    public void updateTagTest() throws SQLException {
        int updatedRows = dbTags.updateTag(999, "New Name");
        Assert.assertTrue(updatedRows == 0, "Не должно быть обновленных строк");
    }

    /**
     * 2.5
     */
    @Test(description = "Позитивный кейс: Удаление тега. Проверка, что тег удален.")
    public void deleteTagTest() throws SQLException {
        int tagId = dbTags.createTag("Test Tag");
        int deletedRows = dbTags.deleteTag(tagId);

        Assert.assertTrue(deletedRows == 1, "Тег не удален");
        ResultSet tag = dbTags.getTagById(tagId);
        Assert.assertFalse(tag.next(), "Тег должен быть удален");
    }

    @Test(description = "Негативный кейс: Удаление несуществующего тега. Проверка сообщения об ошибке.")
    public void deleteTagsTest() throws SQLException {
        int deletedRows = dbTags.deleteTag(999);
        Assert.assertTrue(deletedRows == 0, "Не должно быть удаленных строк");
    }
}