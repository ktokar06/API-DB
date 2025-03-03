package org.example.service;

import java.sql.*;

public class dbTags {

    private static Connection connection;

    public static void setConnection(Connection connection) {
        dbTags.connection = connection;
    }

    public static int createTag(String name) throws SQLException {
        if (name == null || name.isEmpty()) {
            throw new SQLException("Имя тега не может быть пустым");
        }

        String slug = name.toLowerCase().replace(" ", "-");
        String query = "INSERT INTO wp_terms (name, slug) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, slug);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                return generatedKeys.next() ? generatedKeys.getInt(1) : -1;
            }
        }
    }

    public static ResultSet getTagById(int termId) throws SQLException {
        String query = "SELECT * FROM wp_terms WHERE term_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, termId);
        return pstmt.executeQuery();
    }

    public static int updateTag(int termId, String newName) throws SQLException {
        if (newName == null || newName.isEmpty()) {
            throw new SQLException("Новое имя тега не может быть пустым");
        }

        String newSlug = newName.toLowerCase().replace(" ", "-");
        String query = "UPDATE wp_terms SET name = ?, slug = ? WHERE term_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, newSlug);
            pstmt.setInt(3, termId);
            return pstmt.executeUpdate();
        }
    }

    public static int deleteTag(int termId) throws SQLException {
        String query = "DELETE FROM wp_terms WHERE term_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, termId);
            return pstmt.executeUpdate();
        }
    }

    public static void deleteAllTags() throws SQLException {
        String query = "DELETE FROM wp_terms";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        }
    }
}