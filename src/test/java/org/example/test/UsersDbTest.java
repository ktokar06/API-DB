package org.example.test;

import org.example.service.dbUsers;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDbTest extends BaseTest {


    /**
     * 3.1
     */
    @Test(description = "Позитивный кейс: Проверка наличия системного администратора.")
    public void checkAdminExistsTest() throws SQLException {
        ResultSet admin = dbUsers.getUserById(1);
        Assert.assertTrue(admin.next(), "Администратор не найден");
        Assert.assertEquals(admin.getString("user_login"), "Firstname.LastName", "Логин администратора не совпадает");
    }

    /**
     * 3.2
     */
    @Test(description = "Позитивный кейс: Создание пользователя. Проверка, что пользователь создан с корректными параметрами.")
    public void createUserTest() throws SQLException {
        int userId = dbUsers.createUser("testuser", "password123", "test@example.com");
        ResultSet user = dbUsers.getUserById(userId);
        Assert.assertTrue(user.next(), "Пользователь не найден");
        Assert.assertEquals(user.getString("user_login"), "testuser", "Логин не совпадает");
        Assert.assertEquals(user.getString("user_email"), "test@example.com", "Email не совпадает");
    }

    /**
     * 3.3
     */
    @Test(description = "Позитивный кейс: Получение пользователя по ID. Проверка, что пользователь возвращается с корректными параметрами.")
    public void retrieveUserByIDTest() throws SQLException {
        int userId = dbUsers.createUser("user1", "pass1", "user1@test.com");
        ResultSet user = dbUsers.getUserById(userId);
        Assert.assertTrue(user.next(), "Пользователь не найден");
        Assert.assertEquals(user.getString("user_login"), "user1");
    }

    @Test(description = "Негативный кейс: Получение несуществующего пользователя по ID. Проверка сообщения об ошибке.")
    public void retrieveUsersByIdTest() throws SQLException {
        ResultSet user = dbUsers.getUserById(999);
        Assert.assertFalse(user.next(), "Пользователь не должен существовать");
    }

    /**
     * 3.4
     */
    @Test(description = "Позитивный кейс: Обновление пользователя. Проверка, что пользователь обновлен с новыми параметрами.")
    public void updateUserTest() throws SQLException {
        int userId = dbUsers.createUser("olduser", "pass", "old@test.com");
        int updatedRows = dbUsers.updateUser(userId, "newuser", "new@test.com");

        Assert.assertEquals(updatedRows, 1, "Пользователь не обновлен");
        ResultSet user = dbUsers.getUserById(userId);
        Assert.assertTrue(user.next());
        Assert.assertEquals(user.getString("user_login"), "newuser");
        Assert.assertEquals(user.getString("user_email"), "new@test.com");
    }

    @Test(description = "Негативный кейс: Обновление несуществующего пользователя. Проверка сообщения об ошибке.")
    public void updateUsersTest() throws SQLException {
        int updatedRows = dbUsers.updateUser(999, "newuser", "new@test.com");
        Assert.assertEquals(updatedRows, 0, "Не должно быть обновленных строк");
    }

    /**
     * 3.5
     */
    @Test(description = "Позитивный кейс: Удаление пользователя. Проверка, что пользователь удален.")
    public void deleteUserTest() throws SQLException {
        int userId = dbUsers.createUser("todelete", "pass", "delete@test.com");
        int deletedRows = dbUsers.deleteUser(userId);

        Assert.assertEquals(deletedRows, 1, "Пользователь не удален");
        ResultSet user = dbUsers.getUserById(userId);
        Assert.assertFalse(user.next(), "Пользователь должен быть удален");
    }

    @Test(description = "Негативный кейс: Удаление несуществующего пользователя. Проверка сообщения об ошибке.")
    public void deleteUsersTest() throws SQLException {
        int deletedRows = dbUsers.deleteUser(999);
        Assert.assertEquals(deletedRows, 0, "Не должно быть удаленных строк");
    }
}