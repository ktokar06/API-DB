package org.example.test;

import org.example.service.dbPosts;
import org.example.service.dbTags;
import org.example.service.dbUsers;
import org.example.utils.DataBaseConnUtilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.sql.Connection;
import java.sql.SQLException;

public class BaseTest {

    private DataBaseConnUtilities dbUtils;

    @BeforeMethod
    public void setUp() {
        dbUtils = new DataBaseConnUtilities();
        String hostname = "localhost";
        String dbname = "wordpress";
        String username = "wordpress";
        String password = "wordpress";
        Connection connection = dbUtils.createConnection(hostname, dbname, username, password);
        dbPosts.setConnection(connection);
        dbTags.setConnection(connection);
        dbUsers.setConnection(connection);
        dbUtils.createStatement();
    }

    @AfterMethod
    public void tearDown() {
        try {
            dbPosts.deleteAllPosts();
            dbTags.deleteAllTags();
            dbUsers.deleteAllUsersAdmin();
        } catch (SQLException e) {
            System.err.println("Ошибка при очистке данных: " + e.getMessage());
        } finally {
            dbUtils.closeResources();
        }
    }
}