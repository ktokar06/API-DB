package org.example.test;

import org.example.service.dbTags;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.service.dbTags.*;

public class TagsDbTest extends BaseTest {

    private final String nameField = "name";
    private final String slugField= "slug";

    /**
     * 2.1
     */
    @Test(description = "Позитивный кейс: Получение списка тегов. Проверка, что созданный тег присутствует в списке.")
    public void listTagsTest() throws SQLException {
        int tagId = createTag("Test Tag");
        ResultSet tags = dbTags.getTagById(tagId);
        Assert.assertTrue(tags.next(), "Тег не найден в списке");
    }

    @Test(description = "Негативный кейс: Получение списка тегов. Проверка, что список пуст.")
    public void listTagsNonexistentTest() throws SQLException {
        int tagCount = getTagCount();
        Assert.assertEquals(tagCount, 0, "Ожидалось, что список тегов будет пустым");
    }

    /**
     * 2.2
     */
    @Test(description = "Позитивный кейс: Создание тега. Проверка, что тег создан с корректными параметрами.")
    public void createTagTest() throws SQLException {
        String tagName = "Test Tag";
        int tagId = createTag(tagName);

        ResultSet tag = getTagById(tagId);
        Assert.assertTrue(tag.next(), "Тег не найден");
        Assert.assertEquals(tag.getString(nameField), tagName, "Название тега не совпадает");
        Assert.assertEquals(tag.getString(slugField), "test-tag", "Тег не совпадает");
    }

    /**
     * 2.3
     */
    @Test(description = "Позитивный кейс: Получение тега по ID. Проверка, что тег возвращается с корректными параметрами.")
    public void retrieveTagByIDTest() throws SQLException {
        int tagId = createTag("Test Tag");

        ResultSet tag = getTagById(tagId);
        Assert.assertTrue(tag.next(), "Тег не найден");
        Assert.assertEquals(tag.getString(nameField), "Test Tag", "Название тега не совпадает");
    }

    @Test(description = "Негативный кейс: Получение несуществующего тега по ID. Проверка сообщения об ошибке.")
    public void retrieveNonexistentTagByIDTest() throws SQLException {
        ResultSet tag = getTagById(999);
        Assert.assertFalse(tag.next(), "Тег не должен существовать");
        Assert.assertFalse(tag.isBeforeFirst(), "Результат запроса должен быть пустым");
    }

    /**
     * 2.4
     */
    @Test(description = "Позитивный кейс: Обновление тега. Проверка, что тег обновлен с новыми параметрами.")
    public void updateTagTest() throws SQLException {
        String newName = "New Name";

        int tagId = createTag("Old Name");

        int updatedRows = updateTag(tagId, newName);
        Assert.assertEquals(updatedRows, 1, "Тег не обновлен");

        ResultSet tag = getTagById(tagId);
        Assert.assertTrue(tag.next(), "Тег не найден");
        Assert.assertEquals(tag.getString("name"), newName, "Название не обновлено");
        Assert.assertEquals(tag.getString(slugField), "new-name", "Тег не обновлен");
    }

    @Test(description = "Негативный кейс: Обновление несуществующего тега. Проверка сообщения об ошибке.")
    public void updateNonexistentTagTest() throws SQLException {
        int updatedRows = updateTag(999, "New Name");
        Assert.assertEquals(updatedRows, 0, "Не должно быть обновленных строк");
    }

    /**
     * 2.5
     */
    @Test(description = "Позитивный кейс: Удаление тега. Проверка, что тег удален.")
    public void deleteTagTest() throws SQLException {
        int tagId = createTag("Test Tag");

        int deletedRows = deleteTag(tagId);
        Assert.assertEquals(deletedRows, 1, "Тег не удален");

        ResultSet tag = getTagById(tagId);
        Assert.assertFalse(tag.next(), "Тег должен быть удален");
    }

    @Test(description = "Негативный кейс: Удаление несуществующего тега. Проверка сообщения об ошибке.")
    public void deleteNonexistentTagTest() throws SQLException {
        int deletedRows = deleteTag(999);
        Assert.assertEquals(deletedRows, 0, "Не должно быть удаленных строк");
    }
}