package org.example.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.service.dbUsers.*;
import static org.testng.Assert.assertEquals;


public class UsersDbTest extends BaseTest {

    private final String user_login_field = "user_login";
    private final String user_email_field = "user_email";

    /**
     * 3.1
     */
    @Test(description = "Позитивный кейс: Проверка наличия системного администратора.")
    public void checkAdminExistsTest() throws SQLException {
        ResultSet admin = getUserById(1);
        Assert.assertTrue(admin.next(), "Администратор не найден");
        assertEquals(admin.getString(user_login_field), "Firstname.LastName", "Логин администратора не совпадает");
    }

    /**
     * 3.2
     */
    @Test(description = "Позитивный кейс: Создание пользователя. Проверка, что пользователь создан с корректными параметрами.")
    public void createUserWithValidParamsTest() throws SQLException {
        int userId = createUser("tester", "password123", "test@example.com");

        ResultSet user = getUserById(userId);
        Assert.assertTrue(user.next(), "Пользователь не найден");
        assertEquals(user.getString(user_login_field), "tester", "Логин не совпадает");
        assertEquals(user.getString(user_email_field), "test@example.com", "Email не совпадает");
    }

    /**
     * 3.3
     */
    @Test(description = "Позитивный кейс: Получение пользователя по ID. Проверка, что пользователь возвращается с корректными параметрами.")
    public void retrieveUserByIdTest() throws SQLException {
        int userId = createUser("user1", "pass1", "user1@test.com");

        ResultSet user = getUserById(userId);
        Assert.assertTrue(user.next(), "Пользователь не найден");
        assertEquals(user.getString(user_login_field), "user1");
    }

    @Test(description = "Негативный кейс: Получение несуществующего пользователя по ID. Проверка сообщения об ошибке.")
    public void retrieveNonexistentUserByIdTest() throws SQLException {
        ResultSet user = getUserById(999);
        Assert.assertFalse(user.next(), "Пользователь не должен существовать");
        Assert.assertFalse(user.isBeforeFirst(), "Результат запроса должен быть пустым");
    }

    /**
     * 3.4
     */
    @Test(description = "Позитивный кейс: Обновление пользователя. Проверка, что пользователь обновлен с новыми параметрами.")
    public void updateUserWithValidParamsTest() throws SQLException {
        int userId = createUser("olduser", "pass", "old@test.com");

        int updatedRows = updateUser(userId, "newuser", "new@test.com");
        assertEquals(updatedRows, 1, "Пользователь не обновлен");

        ResultSet user = getUserById(userId);
        Assert.assertTrue(user.next());
        assertEquals(user.getString(user_login_field), "newuser");
        assertEquals(user.getString(user_email_field), "new@test.com");
    }

    @Test(description = "Негативный кейс: Обновление несуществующего пользователя. Проверка сообщения об ошибке.")
    public void updateNonexistentUserTest() throws SQLException {
        int updatedRows = updateUser(999, "newuser", "new@test.com");
        assertEquals(updatedRows, 0, "Не должно быть обновленных строк");
    }

    /**
     * 3.5
     */
    @Test(description = "Позитивный кейс: Удаление пользователя. Проверка, что пользователь удален.")
    public void deleteUserTest() throws SQLException {
        int userId = createUser("todelete", "pass", "delete@test.com");

        int deletedRows = deleteUser(userId);
        assertEquals(deletedRows, 1, "Пользователь не удален");

        ResultSet user = getUserById(userId);
        Assert.assertFalse(user.next(), "Пользователь должен быть удален");
    }

    @Test(description = "Негативный кейс: Удаление несуществующего пользователя. Проверка сообщения об ошибке.")
    public void deleteNonexistentUserTest() throws SQLException {
        int deletedRows = deleteUser(999);
        assertEquals(deletedRows, 0, "Не должно быть удаленных строк");
    }

    /**
     * 3.6
     */
    @Test(description = "Позитивный кейс: Получение информации о текущем пользователе")
    public void retrieveCurrentUserInfoTest() throws SQLException {
        int userId = createUser("user_me", "password123", "user.me@example.com");

        ResultSet user = getUserById(userId);
        Assert.assertTrue(user.next(), "Информация о пользователе не найдена");
        assertEquals(user.getString(user_login_field), "user_me", "Логин не совпадает");
        assertEquals(user.getString(user_email_field), "user.me@example.com", "Email не совпадает");
    }

    @Test(description = "Негативный кейс: Попытка получения без авторизации")
    public void retrieveUnauthorizedUserInfoTest() throws SQLException {
        ResultSet user = getUserById(999);
        Assert.assertFalse(user.next(), "Данные пользователя не должны быть доступны");
        Assert.assertEquals(user, 0, "Количество Пользователей должно быть 0");
    }

    /**
     * 3.7
     */
    @Test(description = "Позитивный кейс: Обновление текущего пользователя")
    public void updateCurrentUserInfoTest() throws SQLException {
        int userId = createUser("old_user", "pass123", "old.user@test.com");

        int updatedRows = updateUser(userId, "new_user", "new.user@test.com");
        assertEquals(updatedRows, 1, "Должна быть одна обновлённая запись");

        ResultSet user = getUserById(userId);
        Assert.assertTrue(user.next(), "Пользователь не найден");
        assertEquals(user.getString(user_login_field), "new_user", "Логин не обновлён");
        assertEquals(user.getString(user_email_field), "new.user@test.com", "Email не обновлён");
    }
}