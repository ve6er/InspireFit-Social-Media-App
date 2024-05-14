package com.example.java_project;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CommentDAO {
    // Method to insert a comment into the database
    public void insertComment(Comment comment) {
        String sql = "INSERT INTO comments (post_id, text, date) VALUES (?, ?, ?)";

        try (Connection conn = Jdbc.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameters for the SQL query
            stmt.setInt(1, comment.getPostId());
            stmt.setString(2, comment.getText());
            stmt.setTimestamp(3, Timestamp.valueOf(comment.getDate()));

            // Execute the SQL query
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}