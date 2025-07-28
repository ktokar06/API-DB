package org.example.service;

import java.sql.*;

public class dbUsers {

    private static Connection connection;

    public static void setConnection(Connection connection) {
        dbUsers.connection = connection;
    }

    public static int createUser(String login, String password, String email) throws SQLException {
        String query = "INSERT INTO wp_users (user_login, user_pass, user_email, user_registered) VALUES (?, ?, ?, NOW())";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                return generatedKeys.next() ? generatedKeys.getInt(1) : -1;
            }
        }
    }

    public static ResultSet getUserById(int userId) throws SQLException {
        String query = "SELECT * FROM wp_users WHERE ID = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, userId);
        return pstmt.executeQuery();
    }

    public static int updateUser(int userId, String newLogin, String newEmail) throws SQLException {
        String query = "UPDATE wp_users SET user_login = ?, user_email = ? WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, newLogin);
            pstmt.setString(2, newEmail);
            pstmt.setInt(3, userId);
            return pstmt.executeUpdate();
        }
    }

    public static int deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM wp_users WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate();
        }
    }

    public static void deleteAllUsersAdmin() throws SQLException {
        String query = "DELETE FROM wp_users WHERE ID != 1";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        }
    }
}