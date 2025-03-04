package org.example.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.service.dbUsers.*;

public class UsersDbTest extends BaseTest {

    /**
     * 3.1
     */
    @Test(description = "Позитивный кейс: Проверка наличия системного администратора.")
    public void checkAdminExistsTest() throws SQLException {
        ResultSet admin = getUserById(1);
        Assert.assertTrue(admin.next(), "Администратор не найден");
        Assert.assertTrue(admin.getString("user_login").equals("Firstname.LastName"), "Логин администратора не совпадает");
    }

    /**
     * 3.2
     */
    @Test(description = "Позитивный кейс: Создание пользователя. Проверка, что пользователь создан с корректными параметрами.")
    public void createUserTest() throws SQLException {
        int userId = createUser("testuser", "password123", "test@example.com");

        ResultSet user = getUserById(userId);
        Assert.assertTrue(user.next(), "Пользователь не найден");
        Assert.assertTrue(user.getString("user_login").equals("testuser"), "Логин не совпадает");
        Assert.assertTrue(user.getString("user_email").equals("test@example.com"), "Email не совпадает");
    }


    /**
     * 3.3
     */
    @Test(description = "Позитивный кейс: Получение пользователя по ID. Проверка, что пользователь возвращается с корректными параметрами.")
    public void retrieveUserByIDTest() throws SQLException {
        int userId = createUser("user1", "pass1", "user1@test.com");

        ResultSet user = getUserById(userId);
        Assert.assertTrue(user.next(), "Пользователь не найден");
        Assert.assertTrue(user.getString("user_login").equals("user1"));
    }

    @Test(description = "Негативный кейс: Получение несуществующего пользователя по ID. Проверка сообщения об ошибке.")
    public void retrieveUsersByIdTest() throws SQLException {
        ResultSet user = getUserById(999);
        Assert.assertFalse(user.next(), "Пользователь не должен существовать");
    }

    /**
     * 3.4
     */
    @Test(description = "Позитивный кейс: Обновление пользователя. Проверка, что пользователь обновлен с новыми параметрами.")
    public void updateUserTest() throws SQLException {
        int userId = createUser("olduser", "pass", "old@test.com");

        int updatedRows = updateUser(userId, "newuser", "new@test.com");
        Assert.assertTrue(updatedRows == 1, "Пользователь не обновлен");

        ResultSet user = getUserById(userId);
        Assert.assertTrue(user.next());
        Assert.assertTrue(user.getString("user_login").equals("newuser"));
        Assert.assertTrue(user.getString("user_email").equals("new@test.com"));
    }

    @Test(description = "Негативный кейс: Обновление несуществующего пользователя. Проверка сообщения об ошибке.")
    public void updateUsersTest() throws SQLException {
        int updatedRows = updateUser(999, "newuser", "new@test.com");
        Assert.assertTrue(updatedRows == 0, "Не должно быть обновленных строк");
    }

    /**
     * 3.5
     */
    @Test(description = "Позитивный кейс: Удаление пользователя. Проверка, что пользователь удален.")
    public void deleteUserTest() throws SQLException {
        int userId = createUser("todelete", "pass", "delete@test.com");

        int deletedRows = deleteUser(userId);
        Assert.assertTrue(deletedRows == 1, "Пользователь не удален");

        ResultSet user = getUserById(userId);
        Assert.assertFalse(user.next(), "Пользователь должен быть удален");
    }

    @Test(description = "Негативный кейс: Удаление несуществующего пользователя. Проверка сообщения об ошибке.")
    public void deleteUsersTest() throws SQLException {
        int deletedRows = deleteUser(999);
        Assert.assertTrue(deletedRows == 0, "Не должно быть удаленных строк");
    }
}