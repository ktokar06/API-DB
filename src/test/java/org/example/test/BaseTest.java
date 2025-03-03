package org.example.test;

import org.example.service.dbPosts;
import org.example.service.dbTags;
import org.example.service.dbUsers;
import org.example.utils.DataBaseConnUtilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseTest {

    private DataBaseConnUtilities dbUtils;
    private Connection connection;
    private Statement statement;

    private String hostname = "localhost";
    private String dbname = "wordpress";
    private String username = "wordpress";
    private String password = "wordpress";

    @BeforeMethod
    public void setUp() throws SQLException {
        dbUtils = new DataBaseConnUtilities();
        connection = dbUtils.createConnection(hostname, dbname, username, password);
        dbPosts.setConnection(connection);
        dbTags.setConnection(connection);
        dbUsers.setConnection(connection);
        statement = dbUtils.createStatement();
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