package org.example.service;

import java.sql.*;

public class dbPosts {

    private static Connection connection;

    public static void setConnection(Connection connection) {
        dbPosts.connection = connection;
    }

    // Create поста
    public static int createPost(String title, String content, int authorId, String status) throws SQLException {
        String slug = title.toLowerCase().replace(" ", "-");
        String query = "INSERT INTO wp_posts (post_author, post_content, post_title, post_status, post_name, post_excerpt) " +
                "VALUES (?, ?, ?, ?, ?, '')";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, authorId);
            pstmt.setString(2, content);
            pstmt.setString(3, title);
            pstmt.setString(4, status);
            pstmt.setString(5, slug);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                return generatedKeys.next() ? generatedKeys.getInt(1) : -1;
            }
        }
    }

    // Retv поста по ID
    public static ResultSet getPostById(int postId) throws SQLException {
        String query = "SELECT * FROM wp_posts WHERE ID = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, postId);
        return pstmt.executeQuery();
    }

    // Update поста
    public static int updatePost(int postId, String title, String content) throws SQLException {
        if (title == null || content == null) {
            throw new SQLException("Заголовок и содержание обязательны");
        }

        String query = "UPDATE wp_posts SET post_title = ?, post_content = ?, post_modified = NOW() WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setInt(3, postId);
            return pstmt.executeUpdate();
        }
    }

    // Delete поста
    public static int deletePost(int postId) throws SQLException {
        String query = "DELETE FROM wp_posts WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            return pstmt.executeUpdate();
        }
    }

    // Delete всех постов
    public static void deleteAllPosts() throws SQLException {
        String query = "DELETE FROM wp_posts";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        }
    }
}