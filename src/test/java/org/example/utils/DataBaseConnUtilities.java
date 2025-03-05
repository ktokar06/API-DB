package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseConnUtilities {

    private Connection connection;
    private Statement statement;

    public Connection createConnection(String hostname, String dbname, String userName, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + hostname + ":3306/" + dbname
                    + "?useSSL=false&serverTimezone=UTC";
            connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Соединение с БД установлено.");
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Ошибка подключения к БД", e);
        }
    }

    public void createStatement() {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании Statement", e);
        }
    }

    public void closeResources() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
                System.out.println("Соединение с БД закрыто.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при закрытии ресурсов", e);
        }
    }
}